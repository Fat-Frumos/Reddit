package com.reddit.service.mapper;

import com.reddit.model.dto.SubRedditResponse;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.SubReddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubRedditResponse.class, SubReddit.class})
public interface SubRedditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubRedditResponse toDto(SubReddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    SubReddit toEntity(SubRedditResponse subredditDto);
}