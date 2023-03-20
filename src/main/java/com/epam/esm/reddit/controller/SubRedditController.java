package com.epam.esm.reddit.controller;

import com.epam.esm.reddit.dto.SubRedditDto;
import com.epam.esm.reddit.service.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
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
