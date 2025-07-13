package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.dto.request.CreateCommentRequest;
import com.dominikmiskovic.forumly.dto.response.CommentResponse;
import com.dominikmiskovic.forumly.exception.ResourceNotFoundException;
import com.dominikmiskovic.forumly.model.Comment;
import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.repository.CommentRepository;
import com.dominikmiskovic.forumly.repository.PostRepository;
import com.dominikmiskovic.forumly.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Constructor Injection - Good!
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * NEW: Public method to get a structured tree of comments for a post.
     * This is the method that PostService will call.
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsWithReplies(Long postId) {
        // 1. Fetch all comments for the post in one go.
        List<Comment> allCommentsForPost = commentRepository.findByPostId(postId);

        // 2. Use your existing `buildCommentTree` method to do the hard work.
        // Note: Passing null for currentUser as the current conversion logic doesn't use it.
        // If you later need user-specific info in the DTO, you'll need to pass the user here.
        return buildCommentTree(allCommentsForPost, null);
    }

    // Method to CREATE a new comment
    @Transactional
    public void createComment(CreateCommentRequest request, User authenticatedUser) {
        // 1. Validate that the Post exists
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + request.getPostId()));

        // 2. (Optional but good) Validate that the Parent Comment exists if parentId is provided
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + request.getParentId()));
            // Optional: Check if parentComment belongs to the same post
            if (!parentComment.getPost().getId().equals(post.getId())) {
                throw new IllegalArgumentException("Parent comment does not belong to the specified post.");
            }
        }

        // 3. Create and populate the new Comment entity
        Comment newComment = new Comment();
        newComment.setContent(request.getContent());
        newComment.setPost(post);
        newComment.setAuthor(authenticatedUser); // Set the author from the authenticated user
        newComment.setParentComment(parentComment); // Set parent if it's a reply

        commentRepository.save(newComment);
    }

    // Method to get a comment by its ID
    @Transactional(readOnly = true)
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    // Method to get all comments for a specific post
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findByPostId(postId);
    }

    // Method to get all direct replies to a parent comment
    @Transactional(readOnly = true)
    public List<Comment> getRepliesByParentCommentId(Long parentCommentId) {
        // First check if parent comment exists
        if (!commentRepository.existsById(parentCommentId)) {
            throw new ResourceNotFoundException("Parent comment not found with id: " + parentCommentId);
        }
        return commentRepository.findByParentCommentId(parentCommentId);
    }


    // Method to UPDATE an existing comment TODO: Not implemented yet
    @Transactional
    public Comment updateComment(Long commentId, String newContent, User authenticatedUser) {
        Comment commentToUpdate = getCommentById(commentId);

        // Authorization: Check if the authenticated user is the author of the comment
        if (!commentToUpdate.getAuthor().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("User not authorized to update this comment.");
        }

        commentToUpdate.setContent(newContent);
        return commentRepository.save(commentToUpdate);
    }

    // Method to DELETE a comment TODO: Not implemented yet
    @Transactional
    public void deleteComment(Long commentId, User authenticatedUser) {
        Comment commentToDelete = getCommentById(commentId); // Reuse existing method

        // Authorization: Check if the authenticated user is the author or perhaps a moderator
        if (!commentToDelete.getAuthor().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("User not authorized to delete this comment.");
        }

        // TODO: Create need more complex logic (e.g., mark as "[deleted]" or re-parent replies).
        // Due to CascadeType.ALL on replies, deleting a comment will also delete its replies.
        commentRepository.delete(commentToDelete);
    }

    // Method to efficiently count all comments for a post.
    @Transactional(readOnly = true)
    public long getCommentCountForPost(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    // In a mapper or service method that converts List<Comment> (entities) to List<CommentResponse> (DTOs for top-level)
    public List<CommentResponse> buildCommentTree(List<Comment> allCommentsForPost, User currentUser) {
        Map<Long, CommentResponse> dtoMap = new HashMap<>();
        List<CommentResponse> topLevelDtos = new ArrayList<>();

        // First pass: create all DTOs and map by ID, set depth for top-level
        for (Comment commentEntity : allCommentsForPost) {
            CommentResponse dto = convertToBasicCommentResponse(commentEntity, currentUser); // Sets id, content, author, voteCount etc.
            dtoMap.put(dto.getId(), dto);
            if (commentEntity.getParentComment() == null) {
                dto.setDepth(0); // Set depth for top-level
                topLevelDtos.add(dto);
            }
        }

        // Second pass: build the tree structure and set depth for replies
        for (Comment commentEntity : allCommentsForPost) {
            if (commentEntity.getParentComment() != null) {
                CommentResponse parentDto = dtoMap.get(commentEntity.getParentComment().getId());
                CommentResponse childDto = dtoMap.get(commentEntity.getId());
                if (parentDto != null && childDto != null) {
                    if (parentDto.getReplies() == null) {
                        parentDto.setReplies(new ArrayList<>());
                    }
                    parentDto.getReplies().add(childDto);
                    childDto.setDepth(parentDto.getDepth() + 1); // Set depth for reply
                }
            }
        }
        // Sort replies within each parent if needed (e.g., by createdAt)
        topLevelDtos.forEach(this::sortRepliesRecursively);
        return topLevelDtos;
    }

    private CommentResponse convertToBasicCommentResponse(Comment entity, User currentUser) {
        CommentResponse dto = new CommentResponse();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        if (entity.getAuthor() != null) dto.setAuthorUsername(entity.getAuthor().getUsername());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setVoteCount(entity.getVoteCount()); // From @Transient method

        // Set the parentId if the comment is a reply
        if (entity.getParentComment() != null) {
            dto.setParentId(entity.getParentComment().getId());
        }

        // Initialize replies list
        dto.setReplies(new ArrayList<>());
        return dto;
    }

    private void sortRepliesRecursively(CommentResponse dto) {
        if (dto.getReplies() != null && !dto.getReplies().isEmpty()) {
            dto.getReplies().sort(Comparator.comparing(CommentResponse::getCreatedAt)); // Example sort
            dto.getReplies().forEach(this::sortRepliesRecursively);
        }
    }
}