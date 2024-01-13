package com.epam.esm.reddit.controller;

import com.epam.esm.reddit.model.dto.PostPresentation;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final MeterRegistry meterRegistry;

    @GetMapping
    public ResponseEntity<PostPresentation> getPost(
            @RequestParam(name = "name", defaultValue = "user") String name){
        meterRegistry.counter("name", List.of()).increment();
        meterRegistry.counter("name", List.of(Tag.of("name", name))).increment();
        return ResponseEntity.ok(new PostPresentation(name));
    }
}
