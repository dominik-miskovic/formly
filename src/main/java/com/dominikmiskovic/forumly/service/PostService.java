package com.dominikmiskovic.forumly.service;

import com.dominikmiskovic.forumly.dto.request.CreatePostRequest;
import com.dominikmiskovic.forumly.dto.response.PostDetailResponse;
import com.dominikmiskovic.forumly.dto.response.PostSummaryResponse;
import com.dominikmiskovic.forumly.exception.ResourceNotFoundException;
import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.repository.PostRepository;
import com.dominikmiskovic.forumly.repository.UserRepository;
import com.dominikmiskovic.forumly.repository.VoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       VoteRepository voteRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    // Method to CREATE a new Post
    @Transactional
    public Post createPost(CreatePostRequest request, User authenticatedUser) {
        Post newPost = new Post();
        newPost.setTitle(request.getTitle());
        newPost.setContent(request.getContent());
        newPost.setAuthor(authenticatedUser);

        return postRepository.save(newPost);
    }

    // Method to GET all posts (summaries, paginated)
    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getAllPostSummaries(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable); // Fetches a page of Post entities
        return postPage.map(this::convertToPostSummaryResponse); // Map each Post to PostSummaryResponse
    }

    // Helper method to convert Post entity to PostSummaryResponse DTO
    // This would ideally be in a dedicated Mapper class/component
    private PostSummaryResponse convertToPostSummaryResponse(Post post) {
        PostSummaryResponse dto = new PostSummaryResponse();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        if (post.getAuthor() != null) {
            dto.setAuthorUsername(post.getAuthor().getUsername());
        }
        dto.setCreatedAt(post.getCreatedAt());

        // Efficiently get vote count
        Integer voteCount = voteRepository.sumVoteTypesByPostId(post.getId());
        dto.setVoteCount(voteCount);

        dto.setCommentCount(post.getComments() != null ? post.getComments().size() : 0); // Be wary of N+1 here in lists

        return dto;
    }


    // Method to GET a single Post by ID (detailed view)
    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetailsById(Long id) { // ID should be Long
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        return convertToPostDetailResponse(post);
    }

    // Helper method to convert Post entity to PostDetailResponse DTO
    // This would ideally be in a dedicated Mapper class/component
    private PostDetailResponse convertToPostDetailResponse(Post post) {
        PostDetailResponse dto = new PostDetailResponse();
        // Populate fields from PostSummaryResponse (if not using inheritance for DTOs)
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        if (post.getAuthor() != null) {
            dto.setAuthorUsername(post.getAuthor().getUsername());
        }
        dto.setCreatedAt(post.getCreatedAt());

        // Populate PostDetailResponse specific fields
        dto.setContent(post.getContent());
        dto.setUpdatedAt(post.getUpdatedAt());

        Integer voteCount = voteRepository.sumVoteTypesByPostId(post.getId());
        dto.setVoteCount(voteCount);

        // Populate comments if needed (could be lazy-loaded or fetched separately)
        if (post.getComments() != null) {

            dto.setComments(post.getComments().stream().map(c -> {
                // Simplified mapping - in reality, use a CommentMapper
                com.dominikmiskovic.forumly.dto.response.CommentResponse cr = new com.dominikmiskovic.forumly.dto.response.CommentResponse();
                cr.setId(c.getId());
                cr.setContent(c.getContent());
                if(c.getAuthor() != null) cr.setAuthorUsername(c.getAuthor().getUsername());
                cr.setCreatedAt(c.getCreatedAt());

                cr.setVoteCount(c.getVoteCount()); // Using the @Transient getVoteCount() on Comment entity
                return cr;
            }).collect(Collectors.toList()));
        }
        return dto;
    }


    // Method to UPDATE an existing Post
    @Transactional
    public Post updatePost(Long postId, /* UpdatePostRequest request, */ String newTitle, String newContent, User authenticatedUser) {
        Post postToUpdate = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Authorization check
        if (!postToUpdate.getAuthor().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("User not authorized to update this post."); // Or AccessDeniedException
        }

        postToUpdate.setTitle(newTitle /* request.getTitle() */);
        postToUpdate.setContent(newContent /* request.getContent() */);
        // @UpdateTimestamp handles updatedAt

        return postRepository.save(postToUpdate);
    }

    // Method to DELETE a Post
    @Transactional
    public void deletePost(Long postId, User authenticatedUser) {
        Post postToDelete = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Authorization check
        if (!postToDelete.getAuthor().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("User not authorized to delete this post.");
        }

        // Deleting a post will also delete its comments and votes due to CascadeType.ALL
        postRepository.delete(postToDelete);
    }
}