package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.UserRegistrationDto;
import com.dominikmiskovic.forumly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register") // Map this controller to the /register path
public class RegisterController {

    private final UserService userService; // Declare the UserService field

    // Use constructor injection to inject UserService
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping // Handle GET requests to /register to display the registration form
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto()); // Add an empty UserRegistrationDto to the model for the form
        return "register"; // Return the name of your registration HTML template (register.html)
    }

    @PostMapping // Handle POST requests to /register to process the registration
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.registerUser(registrationDto); // Call the UserService to register the user
        return "redirect:/login"; // Redirect to the login page after successful registration
    }
}