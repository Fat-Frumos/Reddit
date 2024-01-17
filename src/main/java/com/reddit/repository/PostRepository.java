package com.reddit.repository;

import com.reddit.model.entity.Post;
import com.reddit.model.entity.SubReddit;
import com.reddit.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(SubReddit subReddit);
    @EntityGraph(attributePaths = {"subreddit", "user"})
    @Query("SELECT p FROM Post p JOIN FETCH p.subreddit s JOIN FETCH p.user u")
    List<Post> findAllWithSubredditAndUser();

    List<Post> findByUser(User user);
}
