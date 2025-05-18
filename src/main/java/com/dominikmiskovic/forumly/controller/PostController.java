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
    public String viewPost(@PathVariable Long id, Model model, Authentication authentication) {
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
        model.addAttribute("newCommentRequest", new CreateCommentRequest());
        model.addAttribute("postId", id); // For the comment form action

        return "post"; // Renders src/main/resources/templates/post.html
    }

    // --- Creating a new Post ---
    // 1. Show the form (if you have a dedicated page/modal trigger)
    // This is typically handled by the modal in home.html or a /posts/new GET mapping
    @GetMapping("/new")
    public String showCreatePostForm(Model model) {
        model.addAttribute("createPostRequest", new CreatePostRequest());
        model.addAttribute("pageTitle", "Create New Post");
        return "create-post-form"; // TODO e.g., a dedicated page for creating a post
    }

    // 2. Handle the form submission
    @PostMapping
    public String createPost(@Valid @ModelAttribute("createPostRequest") CreatePostRequest createPostRequest,
                             BindingResult bindingResult,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the form view (or handle in modal context)
            // This part is tricky if submitting from a modal on a different page (like home.html)
            // You might need to pass errors back to the original page or handle with JS/AJAX for modals
            model.addAttribute("pageTitle", "Create New Post");
            // If submitting from home.html's modal, redirecting with errors is complex.
            // A dedicated /posts/new page is easier for traditional form handling with errors.
            return "create-post-form"; // Or logic to show modal again with errors
        }

        User currentUser = userService.findUserEntityByUsername(authentication.getName());
        if (currentUser == null) {
            // Should not happen if endpoint is secured
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to create a post.");
            return "redirect:/login";
        }

        Post savedPost = postService.createPost(createPostRequest, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
        return "redirect:/posts/" + savedPost.getId();
    }


    // --- Creating a new Comment on a Post ---
    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable Long postId,
                                @Valid @ModelAttribute("newCommentRequest") CreateCommentRequest createCommentRequest,
                                BindingResult bindingResult,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                Model model) { // Add Model for re-rendering form with errors

        User currentUser = userService.findUserEntityByUsername(authentication.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to comment.");
            return "redirect:/login";
        }

        // Ensure the request's postId matches the path variable and set it if not already
        createCommentRequest.setPostId(postId);

        if (bindingResult.hasErrors()) {
            // If validation errors, re-render the post detail page with the form showing errors
            // We need to re-populate the model for the post detail page
            PostDetailResponse postDetail = postService.getPostDetailsById(postId);
            model.addAttribute("postDetail", postDetail);
            model.addAttribute("timeAgo", new TimeAgoConverter());
            model.addAttribute("currentUser", currentUser);
            // Re-populate user vote status map (or make it part of PostDetailResponse if always needed)
            // ... (logic for userVoteStatusMap as in viewPost method) ...
            model.addAttribute("errorMessage", "Failed to post comment. Please check errors.");
            return "post"; // Return to the post detail page
        }


        commentService.createComment(createCommentRequest, currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Comment posted successfully!");
        return "redirect:/posts/" + postId + "#comments-section"; // Redirect back to the post, possibly to comments anchor
    }
}