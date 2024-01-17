package com.reddit.repository;

import com.reddit.model.entity.Post;
import com.reddit.model.entity.User;
import com.reddit.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);
}
