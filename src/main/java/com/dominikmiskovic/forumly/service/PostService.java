package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void savePost(Post post) {
        postRepository.save(post);
    }
}
