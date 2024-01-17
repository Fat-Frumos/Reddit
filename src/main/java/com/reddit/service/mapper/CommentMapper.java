package com.reddit.service.mapper;

import com.reddit.model.dto.CommentResponse;
import com.reddit.model.entity.Comment;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "dto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post",  source = "post")
    Comment toEntity(CommentResponse dto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentResponse toDto(Comment comment);
}
