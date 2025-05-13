package com.dominikmiskovic.forumly.repository;

import com.dominikmiskovic.forumly.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
