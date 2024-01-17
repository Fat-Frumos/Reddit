package com.reddit.controller;

import com.reddit.model.dto.CommentResponse;
import com.reddit.model.entity.Comment;
import com.reddit.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentService service;

    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CommentResponse dto){
        return service.save(dto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllForPost(
            @PathVariable Long postId){
        return service.getCommentsForPost(postId);
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<List<CommentResponse>> getAllForUser(
            @PathVariable String name){
        return service.getAllCommentsForUser(name);
    }
}
