package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.config.PasswordEncoder;
import com.dominikmiskovic.forumly.model.Topic;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.repository.TopicRepository;
import com.dominikmiskovic.forumly.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    // Use constructor injection to inject UserRepository and PasswordEncoder
    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> loadTopics() {

        return topicRepository.findAll();

    }

    public Topic loadTopic(Integer id) {
        Optional<Topic> topicOptional = topicRepository.findById(id);

        if (topicOptional.isEmpty()) {
            // Throw ResponseStatusException with HTTP status 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found with id: " + id);

        }

        return topicOptional.get();
    }
}
