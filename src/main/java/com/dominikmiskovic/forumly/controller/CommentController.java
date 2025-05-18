package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.request.CreateCommentRequest;
import com.dominikmiskovic.forumly.exception.ResourceNotFoundException;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.service.CommentService;
import com.dominikmiskovic.forumly.service.PostService;
import com.dominikmiskovic.forumly.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts/{postId}/comments") // Base path for comment creation related to a specific post
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService; // To ensure the post exists

    public CommentController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    // This method handles the submission of the "new comment" or "reply to comment" form
    @PostMapping // Mapped to POST /posts/{postId}/comments
    public String createComment(@PathVariable Long postId,
                                @Valid @ModelAttribute("newCommentRequest") CreateCommentRequest createCommentRequest,
                                BindingResult bindingResult,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes,
                                Model model) { // Added Model for re-rendering with errors

        // 1. Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to comment.");
            return "redirect:/login"; // Or return some error view
        }
        User currentUser = userService.findUserEntityByUsername(authentication.getName());
        if (currentUser == null) {
            // Should ideally not happen if authenticated
            redirectAttributes.addFlashAttribute("errorMessage", "User account not found.");
            return "redirect:/home"; // Or an error page
        }

        // 2. Ensure the target Post (for the comment) exists
        // This check can also be done within the commentService.createComment if preferred
        try {
            postService.getPostDetailsById(postId); // Throws ResourceNotFoundException if post doesn't exist
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "The post you are trying to comment on does not exist.");
            return "redirect:/home"; // Or a 404 page
        }

        // 3. Set the postId in the DTO (it's also in the path, but good to have in DTO for service)
        createCommentRequest.setPostId(postId);

        // 4. Validate the DTO
        if (bindingResult.hasErrors()) {
            // If there are validation errors, we need to redirect back to the post detail page
            // and display the errors. This is a bit tricky with redirects.
            // A common way is to use FlashAttributes to pass the DTO and errors.
            // Or, more simply for now, just redirect with a generic error.
            // For a better UX, you'd repopulate the post detail page and show errors next to the form.

            // To re-render the form with errors on the same page (post detail):
            // You would essentially need to duplicate the logic from PostController.viewPost() here
            // to repopulate the model, which is not ideal.
            // This is why AJAX form submissions are often preferred for comments.

            // Simpler redirect with a generic error for now:
            redirectAttributes.addFlashAttribute("commentFormError", "There were errors in your comment. Please try again.");
            redirectAttributes.addFlashAttribute("newCommentRequestWithErrors", createCommentRequest); // Pass back the DTO
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newCommentRequestWithErrors", bindingResult); // Pass back errors

            return "redirect:/posts/" + postId + "#comments-section"; // Redirect back to the form
        }


        // 5. Call the service to create the comment
        try {
            commentService.createComment(createCommentRequest, currentUser);
            redirectAttributes.addFlashAttribute("successMessage", "Comment posted successfully!");
        } catch (ResourceNotFoundException e) { // e.g., if parent comment in DTO not found
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Log the exception
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred while posting your comment.");
        }

        // 6. Redirect back to the post detail page, ideally anchoring to the comments section
        // or even the newly created comment if you can get its ID.
        return "redirect:/posts/" + postId + "#comments-section";
    }

    // You might add methods for editing or deleting comments here later, e.g.:
    // @PostMapping("/{commentId}/edit")
    // @PostMapping("/{commentId}/delete")
}