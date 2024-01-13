package com.epam.esm.reddit.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubRedditDto {

    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
