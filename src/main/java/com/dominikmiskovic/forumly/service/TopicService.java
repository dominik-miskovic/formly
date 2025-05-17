package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.Topic;
import com.dominikmiskovic.forumly.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        Topic topic = topicOptional.get();

        // Set the depth for all posts in the topic
        setPostDepths(topic.getPosts());

        return topic;
    }

    private void setPostDepths(List<Post> posts) {
        if (posts == null) {
            return;
        }

        // First, identify top-level posts (those with no parent)
        List<Post> topLevelPosts = posts.stream()
                .filter(post -> post.getParentPost() == null)
                .toList();

        // Set depth for top-level posts and then recursively for their replies
        for (Post topPost : topLevelPosts) {
            topPost.setDepth(0);
            setReplyDepth(topPost, 1);
        }
    }

    private void setReplyDepth(Post parentPost, int depth) {
        if (parentPost.getReplies() != null) {
            for (Post reply : parentPost.getReplies()) {
                reply.setDepth(depth);
                setReplyDepth(reply, depth + 1); // Recurse for replies of the reply
            }
        }
    }

}
