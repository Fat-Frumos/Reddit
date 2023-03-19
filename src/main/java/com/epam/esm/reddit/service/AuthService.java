package com.epam.esm.reddit.service;

import com.epam.esm.reddit.domain.NotificationEmail;
import com.epam.esm.reddit.domain.Person;
import com.epam.esm.reddit.domain.VerificationToken;
import com.epam.esm.reddit.dto.RegisterRequest;
import com.epam.esm.reddit.repository.UserRepository;
import com.epam.esm.reddit.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        Person person = new Person();
        person.setUsername(registerRequest.getUsername());
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        person.setEmail(registerRequest.getEmail());
        person.setCreatedDate(Instant.now());
        person.setEnabled(false);
        userRepository.save(person);

        String token = generateVerificationToken(person);
        mailService.sendMail(new NotificationEmail("Please activate your Account", person.getEmail(),
                "Click on the below url to activate your account : " +
                        "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(Person person) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setPerson(person);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
