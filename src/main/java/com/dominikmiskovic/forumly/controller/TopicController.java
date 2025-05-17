package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.helper.TimeAgoConverter;
import com.dominikmiskovic.forumly.model.Topic;
import com.dominikmiskovic.forumly.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/topic/{id}")
    public String viewTopic(@PathVariable Integer id, Model model) {

        Topic loadedTopic = topicService.loadTopic(id); // Assuming loadTopics returns a List<Topic>
        model.addAttribute("topic", loadedTopic); // Add topics to the model
        model.addAttribute("timeAgo", new TimeAgoConverter());
        return "topic";
    }
}
