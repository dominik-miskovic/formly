package com.dominikmiskovic.forumly;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // TODO
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("name", "World");
        return "login";
    }
}
