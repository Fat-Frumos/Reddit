package com.epam.esm.reddit.controller;

import com.epam.esm.reddit.model.dto.AuthenticationResponse;
import com.epam.esm.reddit.model.dto.LoginRequest;
import com.epam.esm.reddit.model.dto.RegisterRequest;
import com.epam.esm.reddit.service.JwtAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
