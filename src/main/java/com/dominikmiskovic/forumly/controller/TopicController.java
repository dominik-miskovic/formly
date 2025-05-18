package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.helper.TimeAgoConverter;
import com.dominikmiskovic.forumly.model.Topic;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.service.TopicService;
import com.dominikmiskovic.forumly.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;

    public TopicController(TopicService topicService, UserService userService) {
        this.userService = userService;
        this.topicService = topicService;
    }

    @GetMapping("/topic/{id}")
    public String viewTopic(@PathVariable Integer id, Model model) {

        Topic loadedTopic = topicService.loadTopic(id); // Assuming loadTopics returns a List<Topic>
        model.addAttribute("topic", loadedTopic); // Add topics to the model
        model.addAttribute("timeAgo", new TimeAgoConverter());
        return "topic";
    }

    @PostMapping("/topic")
    public String createTopic(@RequestParam String title,
                             @RequestParam String content) { // Get content from form

        // Get the current logged-in user (using Spring Security example)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Get username
        // Find the user by username (you'll need a method in your UserService)
        User currentUser = userService.findByUsername(currentUsername);

        // Create a new Post object
        Topic newTopic = new Topic();
        newTopic.setTitle(title);
        newTopic.setContent(content);
        newTopic.setAuthor(currentUser);

        // Save the topic and get the saved instance with the generated ID
        Topic savedTopic = topicService.saveTopic(newTopic);

        // Redirect to the topic page using the ID from the saved topic
        return "redirect:/topic/" + savedTopic.getId();
    }
}
