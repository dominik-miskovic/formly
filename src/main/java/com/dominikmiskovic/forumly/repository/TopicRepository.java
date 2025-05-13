package com.dominikmiskovic.forumly.repository;

import com.dominikmiskovic.forumly.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {
}
