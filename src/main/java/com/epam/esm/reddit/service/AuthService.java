package com.epam.esm.reddit.service;

import com.epam.esm.reddit.model.dto.AuthenticationResponse;
import com.epam.esm.reddit.model.dto.LoginRequest;
import com.epam.esm.reddit.model.dto.RegisterRequest;
import com.epam.esm.reddit.model.entity.User;
import com.epam.esm.reddit.model.entity.VerificationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    AuthenticationResponse login(LoginRequest loginRequest);

    ResponseEntity<String> signup(RegisterRequest registerRequest);

    @Transactional
    ResponseEntity<String> verifyAccount(String token);

    User fetchUserAndEnable(VerificationToken token);
}
