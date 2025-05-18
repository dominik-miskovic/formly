package com.dominikmiskovic.forumly.repository;

import com.dominikmiskovic.forumly.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Find posts by a specific author
    List<Post> findByAuthorId(Long authorId);

    // Find posts by title containing a certain string (for searching)
    List<Post> findByTitleContainingIgnoreCase(String titleKeyword);

    // Find posts by author's username
    List<Post> findByAuthorUsername(String username);
}
