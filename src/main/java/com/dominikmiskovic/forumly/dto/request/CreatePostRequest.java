package com.dominikmiskovic.forumly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostRequest {

    @NotBlank(message = "Post title cannot be empty.")
    @Size(min = 3, max = 300, message = "Post title must be between 3 and 300 characters.")
    private String title;

    @NotBlank(message = "Post content cannot be empty.")
    @Size(min = 10, max = 40000, message = "Post content must be between 10 and 40,000 characters.")

    private String content;
}