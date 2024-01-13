package com.epam.esm.reddit.service.facade;

import com.epam.esm.reddit.model.dto.RegisterRequest;
import com.epam.esm.reddit.model.entity.NotificationEmail;
import com.epam.esm.reddit.model.entity.User;
import com.epam.esm.reddit.model.entity.VerificationToken;
import com.epam.esm.reddit.service.MailService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SignupFacadeService implements SignupService {

    private final MailService mailService;
    private final FacadeRepositoryService repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<String> signUp(RegisterRequest request) {
        User user = getUserFromRequest(request);
        repository.saveUser(user);
        String token = createVerificationToken(user);
        mailService.sendMail(new NotificationEmail(
                "Please activate your Account",
                user.getEmail(),
                "Click on the below url to activate your account : "
                        + "http://localhost:8080/api/auth/accountVerification/" + token));
        return new ResponseEntity<>("User successfully registered", HttpStatus.OK);
    }

    @Override
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verification = new VerificationToken(token, user);
        repository.saveToken(verification);
        return token;
    }

    @NonNull
    private User getUserFromRequest(RegisterRequest request) {
        return User.builder()
                   .password(passwordEncoder.encode(request.getPassword()))
                   .email(request.getEmail())
                   .username(request.getUsername())
                   .createdDate(Instant.now())
                   .enabled(false)
                   .build();
    }
}
