package com.dominikmiskovic.forumly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // TODO
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
