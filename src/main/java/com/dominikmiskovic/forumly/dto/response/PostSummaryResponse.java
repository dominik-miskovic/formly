package com.dominikmiskovic.forumly.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class PostSummaryResponse {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private Long authorId;
    private Instant createdAt;
    private Integer voteCount;
    private int commentCount;
}
