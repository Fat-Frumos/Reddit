package com.epam.esm.reddit.service.facade;

import com.epam.esm.reddit.model.dto.AuthenticationResponse;
import com.epam.esm.reddit.model.dto.LoginRequest;
import com.epam.esm.reddit.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginFacadeService
        implements LoginService {

    private final JWTProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        Authentication authenticate = getAuthenticate(request);
        String generateToken = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(generateToken, request.getUsername());
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
}
