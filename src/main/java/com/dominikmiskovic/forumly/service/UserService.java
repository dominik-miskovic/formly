package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.config.PasswordEncoder;
import com.dominikmiskovic.forumly.dto.UserRegistrationDto;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Use constructor injection to inject UserRepository and PasswordEncoder
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void registerUser(UserRegistrationDto userDto) {
        // TODO: Check if user exist
        User user = new User();
        user.setUsername(userDto.getUsername());

        user.setPassword(passwordEncoder.encoder().encode(userDto.getPassword()));

        userRepository.save(user);
    }
}
