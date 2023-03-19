package com.epam.esm.reddit.domain;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    VoteType(int direction) {
    }
}
