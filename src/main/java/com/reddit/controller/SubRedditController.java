package com.reddit.controller;

import com.reddit.model.dto.SubRedditResponse;
import com.reddit.service.SubRedditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subreddit")
public class SubRedditController {

    private final SubRedditService service;

    @PostMapping
    public ResponseEntity<SubRedditResponse> createSubreddit(
            @RequestBody SubRedditResponse response) {
        return service.save(response);
    }

    @GetMapping
    public ResponseEntity<List<SubRedditResponse>> getAllSubreddits() {
        return service.getAll();
    }
}
