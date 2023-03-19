package com.epam.esm.reddit.repository;

import com.epam.esm.reddit.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
