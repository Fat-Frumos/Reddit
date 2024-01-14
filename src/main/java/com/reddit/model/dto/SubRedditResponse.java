package com.reddit.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubRedditResponse {

    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
