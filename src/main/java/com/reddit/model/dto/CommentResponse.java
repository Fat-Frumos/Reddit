package com.reddit.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private String text;
    private String userName;
    private Instant createdDate;
}
