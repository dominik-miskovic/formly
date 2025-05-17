package com.dominikmiskovic.forumly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    // Self-referencing relationship for replies
    @ManyToOne
    @JoinColumn(name = "parent_post_id") // This column will store the ID of the parent post
    private Post parentPost;

    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> replies = new ArrayList<>(); // Collection of replies to this post

    // Voting
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    private int voteCount; // Aggregated vote count

    // Add a transient field to store the depth of the post in the tree (for frontend display)
    @Transient
    private int depth;

}