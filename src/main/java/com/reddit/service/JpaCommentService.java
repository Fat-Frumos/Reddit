package com.reddit.service;

import com.reddit.model.dto.CommentResponse;
import com.reddit.model.entity.Comment;
import com.reddit.model.entity.NotificationEmail;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.User;
import com.reddit.repository.CommentRepository;
import com.reddit.service.facade.FacadeRepositoryService;
import com.reddit.service.mapper.CommentMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JpaCommentService implements CommentService {

    private final CommentMapper mapper;
    private final FacadeRepositoryService facade;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    private static final List<String> swearWords = new ArrayList<>();

    @Override
    public ResponseEntity<Comment> save(CommentResponse dto) {
        Post post = facade.findPostById(dto.getPostId());
        User user = facade.getCurrentUser();
        Comment comment = mapper.toEntity(dto, post, user);
        Comment saved = commentRepository.save(comment);
        String message = mailContentBuilder.build(post.getUser().getUsername());
        sendCommentNotification(message, post.getUser());
        return ResponseEntity.ok().body(saved);
    }

    private void sendCommentNotification(
            String message, User user) {
        mailService.sendMail(new NotificationEmail(
                user.getUsername(), user.getEmail(), message));
    }

    @Override
    public ResponseEntity<List<CommentResponse>> getAllCommentsForUser(String name) {
        return ResponseEntity.ok().body(
                commentRepository.findAllByUser(
                        facade.findByUsername(name)).stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(Long id) {
        return ResponseEntity.ok().body(
                commentRepository.findByPost(
                        facade.findPostById(id)).stream().map(mapper::toDto).toList());
    }

    @Override
    public boolean containsSwearWords(String isSwearWords) {
        return swearWords.stream().anyMatch(isSwearWords::contains);
    }
}
