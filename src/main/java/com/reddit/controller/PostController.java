package com.reddit.controller;

import com.reddit.model.dto.PostRequest;
import com.reddit.model.dto.PostResponse;
import com.reddit.service.PostService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
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
@RequestMapping("/api/post")
public class PostController {

    private final PostService service;
    private final MeterRegistry meterRegistry;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest request){
        return service.save(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable Long id){
        return service.getPostById(id);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPostResponse(){
        return service.getAllPosts();
    }

    @GetMapping("/reddit/{id}")
    public ResponseEntity<List<PostResponse>> getBySubReddit(
            @PathVariable Long id){
        return service.getPostsBySubReddit(id);
    }

    @GetMapping("/user/{name}")
    public ResponseEntity<List<PostResponse>> getByUsername(
            @PathVariable String name){
        meterRegistry.counter("name", List.of()).increment();
        meterRegistry.counter("name", List.of(Tag.of("name", name))).increment();
        return service.getPostsByUsername(name);
    }
}
