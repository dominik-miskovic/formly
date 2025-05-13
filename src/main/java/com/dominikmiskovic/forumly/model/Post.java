package com.dominikmiskovic.forumly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Lob // Use @Lob for large text content of the post
    private String content;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false) // Link to the Topic the post belongs to
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Link to the User who created the post
    private User author;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}