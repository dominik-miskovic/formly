package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.response.PostSummaryResponse;
import com.dominikmiskovic.forumly.helper.TimeAgoConverter;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.model.Vote;
import com.dominikmiskovic.forumly.service.PostService;
import com.dominikmiskovic.forumly.service.UserService;
import com.dominikmiskovic.forumly.service.VoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {

    private final PostService postService;
    private final UserService userService;
    private final VoteService voteService;

    public HomeController(PostService postService, UserService userService, VoteService voteService) {
        this.postService = postService;
        this.userService = userService;
        this.voteService = voteService;
    }

    @GetMapping({"/", "/home"}) // Handles both root and /home
    public String home(Model model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size,
                       @RequestParam(name = "sort", defaultValue = "createdAt,desc") String[] sortParams) {

        // --- Handle Sorting ---
        String sortField = sortParams[0];
        Sort.Direction sortDirection = (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortOrder = Sort.by(sortDirection, sortField);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        // --- Fetch Posts (as DTOs) ---
        Page<PostSummaryResponse> postsPage = postService.getAllPostSummaries(pageable);
        model.addAttribute("postsPage", postsPage);

        // --- TimeAgoConverter ---
        model.addAttribute("timeAgo", new TimeAgoConverter());

        // --- User-Specific Vote Information (consider performance) ---
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser != null && postsPage.hasContent()) {
            Map<Long, Integer> userVoteStatusMap = new HashMap<>();
            for (PostSummaryResponse postSummary : postsPage.getContent()) {

                Optional<Vote> userVote = voteService.getUserVoteForPost(currentUser, postSummary.getId());
                userVote.ifPresent(vote -> userVoteStatusMap.put(postSummary.getId(), vote.getVoteType()));
                // If no vote, the map won't contain an entry for that post.
                // Thymeleaf can check: th:with="voteStatus=${userVoteStatusMap[post.id]}"
                // then th:if="${voteStatus != null && voteStatus == 1}" for upvoted, etc.
            }
            model.addAttribute("userVoteStatusMap", userVoteStatusMap);
        }
        model.addAttribute("currentUser", currentUser); // Add current user to model for greeting, etc.


        // The PostSummaryResponse DTO should already contain `voteCount` and `commentCount`,
        // so you likely don't need to fetch `topicVoteCounts` separately anymore.

        model.addAttribute("pageTitle", "Forumly Home"); // Example title
        return "home"; // Renders src/main/resources/templates/home.html
    }
}