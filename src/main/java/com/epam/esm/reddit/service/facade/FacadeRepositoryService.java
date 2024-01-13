package com.epam.esm.reddit.service.facade;

import com.epam.esm.reddit.exception.SpringRedditException;
import com.epam.esm.reddit.model.entity.User;
import com.epam.esm.reddit.model.entity.VerificationToken;
import com.epam.esm.reddit.repository.UserRepository;
import com.epam.esm.reddit.repository.VerificationTokenRepository;
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
