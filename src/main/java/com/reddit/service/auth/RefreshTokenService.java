package com.reddit.service.auth;

import com.reddit.model.dto.RefreshToken;

import java.time.Instant;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(String username, Instant millis);

    String generateToken(String username, Instant millis);

    RefreshToken generateRefreshToken();

    void validateRefreshToken(String token);

    void deleteToken(String token);
}
