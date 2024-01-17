package com.reddit.service.auth;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.model.dto.RefreshTokenRequest;
import com.reddit.model.dto.RegisterRequest;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    AuthenticationResponse login(LoginRequest loginRequest);

    ResponseEntity<String> signup(RegisterRequest registerRequest);

    ResponseEntity<String> verifyAccount(String token);

    User fetchUserAndEnable(VerificationToken token);

    User getCurrentUser();

    AuthenticationResponse refresh(RefreshTokenRequest request);

    void deleteToken(String refreshToken);
}
