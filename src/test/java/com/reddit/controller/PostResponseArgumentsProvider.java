package com.reddit.controller;

import com.reddit.model.dto.PostResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class PostResponseArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new PostResponse(1L, "Post Name", "https://url.site", "Description", "User 1", "Subreddit Name", 0, 0, "1 day ago", false, false),
                                new PostResponse(2L, "Post Name 2", "https://url2.site2", "Description2", "User 2", "Subreddit Name 2", 0, 0, "2 days ago", false, false)
                        )
                )
        );
    }
}
