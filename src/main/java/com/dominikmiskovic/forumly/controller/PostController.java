package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.Topic;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.service.PostService;
import com.dominikmiskovic.forumly.service.TopicService;
import com.dominikmiskovic.forumly.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PostController {

    private final PostService postService;
    private final TopicService topicService; // Inject TopicService
    private final UserService userService; // Inject UserService (assuming you have one)

    public PostController(PostService postService, TopicService topicService, UserService userService) {
        this.postService = postService;
        this.topicService = topicService;
        this.userService = userService;
    }

    @PostMapping("/topic/{topicId}/post")
    public String createPost(@PathVariable Integer topicId,
                             @RequestParam String content,
                             @RequestParam(required = false) Integer parentPostId) { // Get content from form

        // Get the current logged-in user (using Spring Security example)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Get username
        // Find the user by username (you'll need a method in your UserService)
        User currentUser = userService.findByUsername(currentUsername);


        // Find the topic by ID
        Topic topic = topicService.loadTopic(topicId); // Assuming you have a method to load a single topic

        // Create a new Post object
        Post newPost = new Post();
        newPost.setContent(content);
        newPost.setTopic(topic);
        newPost.setAuthor(currentUser);

        // Set parentPost if parentPostId is provided
        if (parentPostId != null) {
            // Find the parent post by ID
            Post parentPost = postService.findPostById(parentPostId); // You'll need to implement this in PostService
            newPost.setParentPost(parentPost);
        } else {
            // If no parentPostId, it's a top-level post
            newPost.setParentPost(null);
        }


        // Save the post and get the saved instance with the generated ID
        Post savedPost = postService.savePost(newPost);


        // Redirect to the topic page with an anchor to the new post's ID
        return "redirect:/topic/" + topicId + "#post-" + savedPost.getId();

    }
}