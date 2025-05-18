package com.dominikmiskovic.forumly.controller;

import com.dominikmiskovic.forumly.dto.request.VoteRequest;
import com.dominikmiskovic.forumly.exception.ResourceNotFoundException;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.service.UserService;
import com.dominikmiskovic.forumly.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;
    private final UserService userService;

    public VoteController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @PostMapping("/cast") // Different endpoint to distinguish from API
    public String castVoteForm(@RequestParam String targetType,
                               @RequestParam Long targetId,
                               @RequestParam int voteType,
                               Authentication authentication,
                               HttpServletRequest request, // To get referer for redirect
                               RedirectAttributes redirectAttributes) {

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            redirectAttributes.addFlashAttribute("voteError", "User not authenticated. Please login to vote.");
            return "redirect:/login";
        }

        User currentUser = userService.findUserEntityByUsername(authentication.getName());
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("voteError", "Authenticated user details not found.");
            return "redirect:/home"; // Or some error page
        }

        VoteRequest voteRequestDto = new VoteRequest(); // Manually create DTO from params
        if ("POST".equalsIgnoreCase(targetType)) {
            voteRequestDto.setPostId(targetId);
        } else if ("COMMENT".equalsIgnoreCase(targetType)) {
            voteRequestDto.setCommentId(targetId);
        } else {
            redirectAttributes.addFlashAttribute("voteError", "Invalid vote target type.");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/home");
        }
        voteRequestDto.setVoteType(voteType);

        try {
            voteService.castVote(voteRequestDto, currentUser);
            redirectAttributes.addFlashAttribute("voteSuccess", "Vote cast successfully!");
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("voteError", e.getMessage());
        } catch (Exception e) {
            // Log e
            redirectAttributes.addFlashAttribute("voteError", "An unexpected error occurred.");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }
}