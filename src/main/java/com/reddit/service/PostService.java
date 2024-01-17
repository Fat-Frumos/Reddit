package com.reddit.service;

import com.reddit.model.dto.PostRequest;
import com.reddit.model.dto.PostResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    ResponseEntity<PostResponse> getPostById(Long id);

    ResponseEntity<PostResponse> save(PostRequest request);

    ResponseEntity<List<PostResponse>> getAllPosts();

    ResponseEntity<List<PostResponse>> getPostsBySubReddit(Long id);

    ResponseEntity<List<PostResponse>> getPostsByUsername(String name);
}
