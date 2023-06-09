package com.epam.esm.reddit.service;

import com.epam.esm.reddit.domain.NotificationEmail;
import com.epam.esm.reddit.domain.User;
import com.epam.esm.reddit.domain.VerificationToken;
import com.epam.esm.reddit.dto.AuthenticationResponse;
import com.epam.esm.reddit.dto.LoginRequest;
import com.epam.esm.reddit.dto.RegisterRequest;
import com.epam.esm.reddit.exception.SpringRedditException;
import com.epam.esm.reddit.repository.UserRepository;
import com.epam.esm.reddit.repository.VerificationTokenRepository;
import com.epam.esm.reddit.security.JWTProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    private final MailService mailService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your Account", user.getEmail(),
                "Click on the below url to activate your account : " +
                        "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> tokenOptional = verificationTokenRepository.findByToken(token);
        VerificationToken verificationToken =
                tokenOptional.orElseThrow(() ->
                        new SpringRedditException("Invalid verification token"));
        fetchUserAndEnable(verificationToken);

    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user =
                userRepository.findByUsername(username).orElseThrow(() ->
                        new SpringRedditException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String generateToken = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(generateToken, loginRequest.getUsername());
    }
}
