package com.epam.esm.reddit.controller;

import com.epam.esm.reddit.model.dto.SubRedditDto;
import com.epam.esm.reddit.service.SubRedditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubreddit(
            @RequestBody SubRedditDto subRedditDto) {
        return ResponseEntity.status(
                HttpStatus.CREATED).body(
                subRedditService.save(subRedditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDto>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(
                subRedditService.getAll()
        );
    }
}
