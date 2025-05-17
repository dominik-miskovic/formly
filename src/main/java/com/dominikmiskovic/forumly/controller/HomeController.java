package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.helper.TimeAgoConverter;
import com.dominikmiskovic.forumly.model.Topic;
import com.dominikmiskovic.forumly.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final TopicService topicService;

    public HomeController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Topic> loadedTopics = topicService.loadTopics(); // Assuming loadTopics returns a List<Topic>
        model.addAttribute("topics", loadedTopics); // Add topics to the model
        model.addAttribute("timeAgo", new TimeAgoConverter());
        return "home";
    }
}
