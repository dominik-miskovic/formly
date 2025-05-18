package com.dominikmiskovic.forumly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotNull(message = "Post ID cannot be null.")
    private Long postId; // The ID of the Post this comment belongs to.

    @NotBlank(message = "Comment content cannot be empty.")
    @Size(min = 1, max = 10000, message = "Comment content must be between 1 and 10,000 characters.")
    private String content;

    // If null, it's a top-level comment on the post.
    // If provided, it's a reply to the comment with this ID.
    private Long parentId;
}