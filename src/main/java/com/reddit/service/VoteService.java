package com.reddit.service;

import com.reddit.model.dto.VoteResponse;
import com.reddit.model.entity.Vote;
import org.springframework.http.ResponseEntity;

public interface VoteService {
    ResponseEntity<Vote> vote(VoteResponse dto);
}
