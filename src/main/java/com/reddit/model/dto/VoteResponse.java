package com.reddit.model.dto;

import com.reddit.model.entity.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {

    private Long postId;
    private VoteType voteType;
}
