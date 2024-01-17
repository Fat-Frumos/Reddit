package com.reddit.service.facade;

import com.reddit.exception.PostNotFoundException;
import com.reddit.exception.SpringRedditException;
import com.reddit.exception.SubRedditNotFoundException;
import com.reddit.exception.UsernameNotFoundException;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.SubReddit;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import com.reddit.repository.PostRepository;
import com.reddit.repository.SubRedditRepository;
import com.reddit.repository.UserRepository;
import com.reddit.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FacadeRepositoryService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final VerificationTokenRepository tokenRepository;
    private final SubRedditRepository subRedditRepository;

    public List<Post> findAllPosts() {
        return postRepository.findAllWithSubredditAndUser();
    }

    public List<Post> findPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    public List<Post> findAllPostsBySubreddit(
            SubReddit subReddit) {
        return postRepository
                .findAllBySubreddit(subReddit);
    }

    public Post findPostById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post NotFound Exception" + id));
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public VerificationToken findByToken(String token) {
        return tokenRepository
                .findByToken(token)
                .orElseThrow(() -> new SpringRedditException(
                        "Invalid verification token"));
    }

    public VerificationToken saveToken(
            VerificationToken token) {
        return tokenRepository.save(token);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found" + username));
    }

    public SubReddit findSubRedditByName(String subredditName) {
        return subRedditRepository
                .findByName(subredditName)
                .orElseThrow(() -> new SubRedditNotFoundException(
                        "SubReddit Not Found" + subredditName));
    }

    public SubReddit findSubRedditById(Long id) {
        return subRedditRepository
                .findById(id)
                .orElseThrow(() -> new SubRedditNotFoundException(
                        "SubReddit Not Found" + id));
    }

    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return findByUsername(principal.getSubject());
    }
}
