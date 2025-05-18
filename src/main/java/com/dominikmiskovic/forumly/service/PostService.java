package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.repository.PostRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Post findPostById(Integer id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + id);
        }

        return postOptional.get();
    }
}
