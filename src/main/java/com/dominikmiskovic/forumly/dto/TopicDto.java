package com.dominikmiskovic.forumly.dto;

import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
