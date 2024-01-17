package com.reddit.service.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.reddit.model.dto.PostRequest;
import com.reddit.model.dto.PostResponse;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.SubReddit;
import com.reddit.model.entity.User;
import com.reddit.repository.CommentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository repository;

    @Mapping(target = "postId", ignore = true)
    @Mapping(target = "subreddit", source = "subReddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    public abstract Post toEntity(PostRequest postRequest, SubReddit subReddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse toDto(Post post);

    Integer commentCount(Post post) {return repository.findByPost(post).size();}
    String getDuration(Post post) {return TimeAgo.using(post.getCreatedDate().toEpochMilli());}
}
