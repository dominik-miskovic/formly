package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.dto.request.VoteRequest;
import com.dominikmiskovic.forumly.exception.ResourceNotFoundException;
import com.dominikmiskovic.forumly.model.Comment;
import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.model.Vote;
import com.dominikmiskovic.forumly.repository.CommentRepository;
import com.dominikmiskovic.forumly.repository.PostRepository;
import com.dominikmiskovic.forumly.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;         // For Post entities
    private final CommentRepository commentRepository;   // For Comment entities

    public VoteService(VoteRepository voteRepository,
                       PostRepository postRepository,
                       CommentRepository commentRepository) {
        this.voteRepository = voteRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void castVote(VoteRequest voteRequest, User user) {
        // Validate voteType (e.g., must be 1 or -1)
        if (voteRequest.getVoteType() != 1 && voteRequest.getVoteType() != -1) {
            throw new IllegalArgumentException("Invalid vote type. Must be 1 (upvote) or -1 (downvote).");
        }

        Post targetPost = null;
        Comment targetComment = null;

        // Determine target and validate
        if (voteRequest.getPostId() != null && voteRequest.getCommentId() != null) {
            throw new IllegalArgumentException("Vote cannot be for both a post and a comment.");
        } else if (voteRequest.getPostId() != null) {
            targetPost = postRepository.findById(voteRequest.getPostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + voteRequest.getPostId()));
        } else if (voteRequest.getCommentId() != null) {
            targetComment = commentRepository.findById(voteRequest.getCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + voteRequest.getCommentId()));
        } else {
            throw new IllegalArgumentException("Vote must target either a post or a comment.");
        }

        Optional<Vote> existingVoteOpt;
        if (targetPost != null) {
            existingVoteOpt = voteRepository.findByUserAndPost(user, targetPost);
        } else { // targetComment != null
            existingVoteOpt = voteRepository.findByUserAndComment(user, targetComment);
        }

        if (existingVoteOpt.isPresent()) {
            Vote vote = existingVoteOpt.get();
            if (vote.getVoteType() == voteRequest.getVoteType()) {
                // User clicked the same vote again, remove the vote (unvote)
                voteRepository.delete(vote);
            } else {
                // User changed their vote (e.g., from upvote to downvote)
                vote.setVoteType(voteRequest.getVoteType());
                voteRepository.save(vote);
            }
        } else {
            // New vote
            Vote newVote = new Vote();
            newVote.setUser(user);
            newVote.setVoteType(voteRequest.getVoteType());
            if (targetPost != null) {
                newVote.setPost(targetPost);
            } else {
                newVote.setComment(targetComment);
            }
            voteRepository.save(newVote);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Vote> getUserVoteForPost(User user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return voteRepository.findByUserAndPost(user, post);
    }

    @Transactional(readOnly = true)
    public Optional<Vote> getUserVoteForComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        return voteRepository.findByUserAndComment(user, comment);
    }

    @Transactional(readOnly = true)
    public int getVoteCountForPost(Long postId) {
        // Ensure post exists, otherwise sumVoteTypesByPostId might return 0 for a non-existent post
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return voteRepository.sumVoteTypesByPostId(postId); // Repository method returns Integer (non-null due to COALESCE)
    }

    @Transactional(readOnly = true)
    public int getVoteCountForComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId);
        }
        return voteRepository.sumVoteTypesByCommentId(commentId); // Repository method returns Integer
    }
}