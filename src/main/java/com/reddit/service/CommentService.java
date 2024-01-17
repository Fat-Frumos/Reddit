package com.reddit.service;

import com.reddit.model.dto.CommentResponse;
import com.reddit.model.entity.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    ResponseEntity<Comment> save(CommentResponse dto);

    ResponseEntity<List<CommentResponse>> getAllCommentsForUser(String name);

    ResponseEntity<List<CommentResponse>> getCommentsForPost(Long id);
}
