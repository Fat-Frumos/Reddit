package com.reddit.controller;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.model.dto.RefreshTokenRequest;
import com.reddit.model.dto.RegisterRequest;
import com.reddit.service.auth.JwtAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Validated
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(
            @RequestBody RegisterRequest registerRequest) {
        return authService.signup(registerRequest);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(
            @PathVariable String token) {
        return authService.verifyAccount(token);
    }

    @PostMapping("/login")
    public AuthenticationResponse loginUser(
            @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(
            @Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(
            @Valid @RequestBody RefreshTokenRequest request) {
        authService.deleteToken(request.getRefreshToken());
        return ResponseEntity.status(OK).body("Token Deleted Successfully!!");
    }
}
