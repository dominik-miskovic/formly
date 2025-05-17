package com.dominikmiskovic.forumly.dto;

import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class TopicDto {
    private int id;
    private String title;
    private String content;
    private User author;
    private List<Post> posts; // TODO: Count of the posts
    private Instant createdAt;
    private Instant updatedAt;
}
