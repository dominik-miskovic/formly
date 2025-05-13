package com.dominikmiskovic.forumly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String username;
    private String password;
    private String confirmPassword;
}
