package com.dominikmiskovic.forumly.repository;

import com.dominikmiskovic.forumly.model.Comment;
import com.dominikmiskovic.forumly.model.Post;
import com.dominikmiskovic.forumly.model.User;
import com.dominikmiskovic.forumly.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // These are good for checking if a specific user has voted on a specific entity instance.
    Optional<Vote> findByUserAndPost(User user, Post post);
    Optional<Vote> findByUserAndComment(User user, Comment comment);

    // TODO: Maybe just work with Ids
    Optional<Vote> findByUserIdAndPostId(Long userId, Long postId);
    Optional<Vote> findByUserIdAndCommentId(Long userId, Long commentId);

    // These @Query methods are for calculating the total vote score.
    @Query("SELECT COALESCE(SUM(v.voteType), 0) FROM Vote v WHERE v.post.id = :postId")
    Integer sumVoteTypesByPostId(@Param("postId") Long postId);

    @Query("SELECT COALESCE(SUM(v.voteType), 0) FROM Vote v WHERE v.comment.id = :commentId")
    Integer sumVoteTypesByCommentId(@Param("commentId") Long commentId);
}
