package com.dominikmiskovic.forumly.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String authorUsername;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer voteCount;
    private Long parentId; // ID of the parent comment, if this is a reply
    private List<CommentResponse> replies; // List of direct replies to this comment
    private int depth; // For client side rendering
}