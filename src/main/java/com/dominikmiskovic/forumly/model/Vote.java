package com.dominikmiskovic.forumly.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "vote")
@Getter
@Setter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id") // Can be null if the vote is for a topic
    private Post post;

    @ManyToOne
    @JoinColumn(name = "topic_id") // Can be null if the vote is for a post
    private Topic topic;

    private int voteType; // 1 for upvote, -1 for downvote

    @CreationTimestamp
    private Instant createdAt;

}
