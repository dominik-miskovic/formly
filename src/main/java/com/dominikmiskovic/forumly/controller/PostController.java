package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.request.CreateCommentRequest;
import com.dominikmiskovic.forumly.dto.request.CreatePostRequest;
import com.dominikmiskovic.forumly.dto.response.CommentResponse;
import com.dominikmiskovic.forumly.dto.response.PostDetailResponse;
import com.dominikmiskovic.forumly.helper.TimeAgoConverter;
import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.model.Vote;
import com.dominikmiskovic.forumly.service.CommentService;
import com.dominikmiskovic.forumly.service.PostService;
import com.dominikmiskovic.forumly.service.UserService;
import com.dominikmiskovic.forumly.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final VoteService voteService;
    private final CommentService commentService;



    public PostController(PostService postService, UserService userService,
                          VoteService voteService, CommentService commentService
    ) {
        this.postService = postService;
        this.userService = userService;
        this.voteService = voteService;
        this.commentService = commentService;
    }

    // --- Displaying a single Post and its Comments ---
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model, Authentication authentication,
                           @ModelAttribute("newCommentRequestWithErrors") CreateCommentRequest commentRequestFromRedirect) {
        // Fetch the PostDetailResponse DTO from the service

        PostDetailResponse postDetail = postService.getPostDetailsById(id);
        model.addAttribute("postDetail", postDetail);
        model.addAttribute("timeAgo", new TimeAgoConverter());

        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userService.findUserEntityByUsername(authentication.getName());
        }
        model.addAttribute("currentUser", currentUser);


        // Prepare user's vote status for the main post and its comments
        Map<String, Map<Long, Integer>> userVoteStatusMap = new HashMap<>();
        Map<Long, Integer> postVotes = new HashMap<>();
        Map<Long, Integer> commentVotes = new HashMap<>();

        if (currentUser != null) {
            // User's vote on the main post
            Optional<Vote> userPostVote = voteService.getUserVoteForPost(currentUser, postDetail.getId());
            userPostVote.ifPresent(vote -> postVotes.put(postDetail.getId(), vote.getVoteType()));

            // User's vote on each comment
            if (postDetail.getComments() != null) {
                for (CommentResponse comment : postDetail.getComments()) {
                    Optional<Vote> userCommentVote = voteService.getUserVoteForComment(currentUser, comment.getId());
                    userCommentVote.ifPresent(vote -> commentVotes.put(comment.getId(), vote.getVoteType()));
                }
            }
        }
        userVoteStatusMap.put("POST", postVotes);
        userVoteStatusMap.put("COMMENT", commentVotes);
        model.addAttribute("userVoteStatusMap", userVoteStatusMap);

        // For the new comment form
        if (!model.containsAttribute("newCommentRequest")) { // If not already populated by a redirect with errors
            model.addAttribute("newCommentRequest", new CreateCommentRequest());
        }
        model.addAttribute("postId", id);

        return "post"; // Renders src/main/resources/templates/post.html
    }

    // Handle the form submission
    @PostMapping
    public String createPost(@Valid @ModelAttribute("createPostRequest") CreatePostRequest createPostRequest,
                             BindingResult bindingResult,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to create a post.");
            return "redirect:/login";
        }

        User currentUser = userService.findUserEntityByUsername(authentication.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User account not found.");
            return "redirect:/home"; // Or an error page
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createPostRequest", createPostRequest); // Send back the DTO with user's input
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createPostRequest", bindingResult); // Send back errors
            redirectAttributes.addFlashAttribute("postCreationError", "Please correct the errors in the form.");
            return "redirect:" + ( model.asMap().get("javax.servlet.forward.request_uri") != null ? ((HttpServletRequest) model.asMap().get("javax.servlet.forward.request_uri")).getHeader("Referer") : "/home" );
        }

        try {
            Post savedPost = postService.createPost(createPostRequest, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
            return "redirect:/posts/" + savedPost.getId(); // Redirect to the new post's detail page
        } catch (Exception e) {
            // Log e
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while creating the post.");
            // Decide where to redirect on a generic error
            return "redirect:/home";
        }
    }
}