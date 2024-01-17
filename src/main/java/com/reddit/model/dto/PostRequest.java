package com.reddit.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostRequest {
    private Long id;
    private String url;
    private String postName;
    private String username;
    private String duration;
    private Integer voteCount;
    private String description;
    private String subredditName;
    private Integer commentCount;
}
