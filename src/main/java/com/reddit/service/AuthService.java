package com.reddit.service;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.model.dto.RegisterRequest;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    AuthenticationResponse login(LoginRequest loginRequest);

    ResponseEntity<String> signup(RegisterRequest registerRequest);

    @Transactional
    ResponseEntity<String> verifyAccount(String token);

    User fetchUserAndEnable(VerificationToken token);
}
