package com.reddit.service;

import com.reddit.model.dto.PostRequest;
import com.reddit.model.dto.PostResponse;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.SubReddit;
import com.reddit.model.entity.User;
import com.reddit.service.auth.AuthService;
import com.reddit.service.facade.FacadeRepositoryService;
import com.reddit.service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class JpaPostService
        implements PostService {

    private final PostMapper mapper;
    private final AuthService service;
    private final FacadeRepositoryService facade;

    @Override
    @Transactional
    public ResponseEntity<PostResponse> save(PostRequest request) {
        SubReddit subReddit = facade.findSubRedditByName(request.getSubredditName());
        User user = service.getCurrentUser();
        Post savedPost = facade.savePost(mapper.toEntity(request, subReddit, user));
        return ResponseEntity.ok(mapper.toDto(savedPost));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = facade
                .findAllPosts()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.status(OK).body(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<PostResponse> getPostById(Long id) {
        return ResponseEntity.ok(mapper.toDto(
                facade.findPostById(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponse>> getPostsBySubReddit(Long id) {
        SubReddit subReddit = facade.findSubRedditById(id);
        List<PostResponse> posts = facade
                .findAllPostsBySubreddit(subReddit)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.status(OK).body(posts);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String name) {
        User user = facade.findByUsername(name);
        List<PostResponse> posts = facade
                .findPostsByUser(user)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.status(OK).body(posts);
    }
}
