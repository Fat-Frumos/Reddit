package com.epam.esm.reddit.repository;

import com.epam.esm.reddit.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
