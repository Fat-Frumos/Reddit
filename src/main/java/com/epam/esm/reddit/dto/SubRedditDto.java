package com.epam.esm.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubRedditDto {

    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
