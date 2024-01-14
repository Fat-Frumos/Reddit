package com.reddit.service;

import com.reddit.model.dto.SubRedditResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubRedditService {
    ResponseEntity<SubRedditResponse> save(SubRedditResponse response);

    ResponseEntity<List<SubRedditResponse>> getAll();
}
