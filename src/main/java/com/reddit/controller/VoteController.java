package com.reddit.controller;

import com.reddit.model.dto.VoteResponse;
import com.reddit.model.entity.Vote;
import com.reddit.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService service;

    @PostMapping
    public ResponseEntity<Vote> createVote(
            @RequestBody VoteResponse dto){
        return service.vote(dto);
    }
}
