package com.dominikmiskovic.forumly.repository;

import com.dominikmiskovic.forumly.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Find all comments for a specific post
    List<Comment> findByPostId(Long postId);

    // Find all comments by a specific author
    List<Comment> findByAuthorId(Long authorId);

    // Find all direct replies to a parent comment
    List<Comment> findByParentCommentId(Long parentCommentId);

    // Find all top-level comments for a post (comments without a parent)
    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);
}
