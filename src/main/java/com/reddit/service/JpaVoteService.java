package com.reddit.service;

import com.reddit.exception.DuplicateVoteException;
import com.reddit.model.dto.VoteResponse;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.Vote;
import com.reddit.repository.VoteRepository;
import com.reddit.service.facade.FacadeRepositoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.reddit.model.entity.VoteType.UPVOTE;

@Service
@RequiredArgsConstructor
public class JpaVoteService
        implements VoteService {

    private final VoteRepository repository;
    private final FacadeRepositoryService facade;

    @Override
    public ResponseEntity<Vote> vote(VoteResponse dto) {
        Post post = facade.savePost(loadPost(dto));
        return ResponseEntity.ok().body(repository.save(toDto(dto, post)));
    }

    @NonNull
    private Post loadPost(VoteResponse dto) {
        Post loadedPost = facade.findPostById(getPost(dto).getPostId());
        loadedPost.setVoteCount(UPVOTE.equals(dto.getVoteType())
                                ? loadedPost.getVoteCount() + 1
                                : loadedPost.getVoteCount() - 1);
        return loadedPost;
    }

    private Post getPost(VoteResponse dto) {
        Post post = facade.findPostById(dto.getPostId());
        Optional<Vote> vote = repository.findTopByPostAndUserOrderByVoteIdDesc(post, facade.getCurrentUser());
        if (vote.isPresent() && vote.get().getVoteType().equals(dto.getVoteType())) {
            throw new DuplicateVoteException("Duplicate vote of type: " + dto.getVoteType().name());
        }
        return post;
    }

    private Vote toDto(VoteResponse response, Post post) {
        return Vote.builder()
                .voteType(response.getVoteType())
                .post(post)
                .user(facade.getCurrentUser())
                .build();
    }
}
