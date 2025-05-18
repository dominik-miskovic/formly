package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.request.VoteRequest;
import com.dominikmiskovic.forumly.exception.ResourceNotFoundException;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.service.UserService;
import com.dominikmiskovic.forumly.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;
    private final UserService userService;

    public VoteController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @PostMapping("/cast") // Endpoint: POST /api/votes/cast
    public ResponseEntity<?> castVote(@Valid @RequestBody VoteRequest voteRequest, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated. Please login to vote."));
        }
        // name = username
        User currentUser = userService.findUserEntityByUsername(authentication.getName());
        if (currentUser == null) {
            // This case should ideally not happen if authentication.isAuthenticated() is true
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Authenticated user details not found."));
        }

        try {
            voteService.castVote(voteRequest, currentUser);

            // After successfully casting the vote, get the new total vote count for the target
            int newVoteCount;
            String targetType; // For the response

            if (voteRequest.getPostId() != null) {
                newVoteCount = voteService.getVoteCountForPost(voteRequest.getPostId());
                targetType = "POST";
            } else if (voteRequest.getCommentId() != null) {
                newVoteCount = voteService.getVoteCountForComment(voteRequest.getCommentId());
                targetType = "COMMENT";
            } else {
                // This case should have been caught by voteService.castVote's internal validation
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid vote request: missing postId or commentId."));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Vote cast successfully.");
            response.put("newVoteCount", newVoteCount);
            response.put("targetId", voteRequest.getPostId() != null ? voteRequest.getPostId() : voteRequest.getCommentId());
            response.put("targetType", targetType);
            response.put("userVoteType", voteRequest.getVoteType()); // The vote type just cast

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) { // If voteService ever throws this for some reason
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Log the exception for internal review
            // logger.error("Error casting vote: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred while casting your vote."));
        }
    }
}