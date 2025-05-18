package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.dto.request.UserRegistrationRequest;
import com.dominikmiskovic.forumly.exception.UserAlreadyExistsException;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Use Spring Security's PasswordEncoder

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional // Make transactional as it involves a DB write and a read
    public User registerUser(UserRegistrationRequest registrationRequest) throws UserAlreadyExistsException { // Changed param name
        // 1. Check if username already exists
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already taken: " + registrationRequest.getUsername());
        }

        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        // TODO: Add other fields like email, firstName, lastName if present in UserRegistrationRequest and User entity

        // Encode the password using Spring Security's PasswordEncoder
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        // TODO: Assign roles/authorities here if you have a more complex role system
        // For now, "ROLE_USER" is hardcoded in loadUserByUsername, which is simple.

        return userRepository.save(newUser); // Return the saved User entity
    }

    // Implementation of UserDetailsService
    @Override
    @Transactional(readOnly = true) // Good for read operations
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // TODO: Fetch user's actual authorities/roles from the database if you have a role system
        // For example, if User entity has a Set<Role> roles field:
        // List<GrantedAuthority> authorities = user.getRoles().stream()
        //         .map(role -> new SimpleGrantedAuthority(role.getName()))
        //         .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // This MUST be the encoded password from the database
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Simple default role
                // authorities // Use this if you have dynamic roles
        );
    }

    // Utility method to find a user by username (returns User entity)
    @Transactional(readOnly = true)
    public User findUserEntityByUsername(String username) { // Renamed for clarity vs. loadUserByUsername
        return userRepository.findByUsername(username)
                .orElse(null); // Returning null here is a design choice.
        // Could also throw ResourceNotFoundException.
    }

    // Utility method to get the currently authenticated User entity
    @Transactional(readOnly = true)
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null; // No user authenticated or it's the anonymous user
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal; // Sometimes the principal is just the username string
        } else {
            return null; // Unknown principal type
        }

        return findUserEntityByUsername(username);
    }

    // Optional: Method to get user by ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElse(null); // Or throw ResourceNotFoundException
    }
}