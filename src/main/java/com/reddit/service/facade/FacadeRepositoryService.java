package com.reddit.service.facade;

import com.reddit.exception.SpringRedditException;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import com.reddit.repository.UserRepository;
import com.reddit.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FacadeRepositoryService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    public VerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(
                () -> new SpringRedditException("Invalid verification token"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new SpringRedditException("User not found"));
    }

    public void saveToken(VerificationToken verification) {
        tokenRepository.save(verification);
    }
}
