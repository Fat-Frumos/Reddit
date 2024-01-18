package com.reddit.service.facade;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.security.JWTProvider;
import com.reddit.service.auth.JwtRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginFacadeService
        implements LoginService {

    private final JWTProvider jwtProvider;
    private final JwtRefreshTokenService service;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse loginRequest(LoginRequest request) {
        Authentication authenticate = getAuthenticate(request);
        String token = generateToken(authenticate);
        return AuthenticationResponse.builder()
                .username(request.getUsername())
                .refreshToken(service.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getExpiration()))
                .authenticationToken(token).build();
    }

    private String generateToken(Authentication authenticate) {
        return jwtProvider.generateToken(authenticate);
    }

    private Authentication getAuthenticate(LoginRequest request) {
        Authentication authenticate = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return authenticate;
    }

    private Authentication getAuthentication(LoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));
    }

    public Instant getExpiration(){
        return Instant.ofEpochSecond(jwtProvider.getExpiration());
    }
}
