package com.dominikmiskovic.forumly.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"}),
        @UniqueConstraint(columnNames = {"user_id", "comment_id"})
})
@Getter
@Setter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id") // Can be null if the vote is for a comment
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id") // Can be null if the vote is for a post
    private Comment comment;

    private int voteType; // 1 for upvote, -1 for downvote, or vote gets deleted

    @CreationTimestamp
    private Instant createdAt;

}
