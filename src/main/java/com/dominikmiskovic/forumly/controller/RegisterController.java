package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.request.UserRegistrationRequest;
import com.dominikmiskovic.forumly.exception.UserAlreadyExistsException;
import com.dominikmiskovic.forumly.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        // Add an empty UserRegistrationRequest to the model for form binding
        if (!model.containsAttribute("userRegistrationRequest")) { // Check if not already added by a redirect with errors
            model.addAttribute("userRegistrationRequest", new UserRegistrationRequest());
        }
        model.addAttribute("pageTitle", "Register");
        return "register"; // Renders src/main/resources/templates/register.html
    }

    @PostMapping
    public String registerUserAccount(
            @Valid @ModelAttribute("userRegistrationRequest") UserRegistrationRequest registrationRequest, // Changed DTO name for consistency
            BindingResult bindingResult, // For capturing validation errors
            RedirectAttributes redirectAttributes,
            Model model) { // Added Model for re-rendering form with errors

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Register");
            // No need to add userRegistrationRequest again, @ModelAttribute does it.
            // BindingResult is automatically added to the model by Spring.
            return "register"; // Return to the registration form to display errors
        }

        try {
            userService.registerUser(registrationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
            return "redirect:/login"; // Redirect to login after successful registration
        } catch (UserAlreadyExistsException e) {
            // If username or email is already taken
            // bindingResult.rejectValue("username", "user.exists", e.getMessage()); // Add error to specific field
            // Or add a global error
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("registrationError", e.getMessage()); // Add a general error message
            // model.addAttribute("userRegistrationRequest", registrationRequest); // Already present due to @ModelAttribute
            return "register"; // Return to the registration form
        } catch (Exception e) {
            // Log the exception for internal review
            // logger.error("Unexpected error during registration: ", e);
            model.addAttribute("pageTitle", "Register");
            model.addAttribute("registrationError", "An unexpected error occurred. Please try again.");
            // model.addAttribute("userRegistrationRequest", registrationRequest);
            return "register";
        }
    }
}