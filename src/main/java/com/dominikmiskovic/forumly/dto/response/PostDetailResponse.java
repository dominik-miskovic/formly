package com.dominikmiskovic.forumly.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true) // Ensures superclass fields are in toString
@EqualsAndHashCode(callSuper = true) // Ensures superclass fields are in equals/hashCode
public class PostDetailResponse extends PostSummaryResponse {
    private List<CommentResponse> comments;
    private Instant updatedAt;
}
