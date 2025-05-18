package com.dominikmiskovic.forumly.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {

    // ID of the Post entity to vote on (optional, one of postId or commentId must be present)
    private Long postId;

    // ID of the Comment entity to vote on (optional)
    private Long commentId;

    @NotNull(message = "Vote type cannot be null.")
    @Min(value = -1, message = "Vote type must be 1 (upvote) or -1 (downvote).")
    @Max(value = 1, message = "Vote type must be 1 (upvote) or -1 (downvote).")

    private int voteType;
}