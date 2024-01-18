package com.reddit.security;

import com.reddit.model.dto.RefreshToken;
import com.reddit.repository.RefreshTokenRepository;
import com.reddit.service.auth.JwtRefreshTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class JWTProviderTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private JwtRefreshTokenService jwtRefreshTokenService;

    @Test
    void testGenerateRefreshToken() {
        String username = "testUser";
        Instant expiryTime = Instant.now().plusMillis(60000);
        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken(UUID.randomUUID().toString());
        mockRefreshToken.setCreatedDate(Instant.now());
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(mockRefreshToken);
        RefreshToken refreshToken = jwtRefreshTokenService.generateRefreshToken(username, expiryTime);
        assertNotNull(refreshToken);
        assertNotEquals(username, refreshToken.getToken());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void testValidateRefreshToken() {
        String token = "testToken";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        when(refreshTokenRepository.findByToken(token)).thenReturn(java.util.Optional.of(refreshToken));
        assertDoesNotThrow(() -> jwtRefreshTokenService.validateRefreshToken(token));
    }

    @Test
    void testDeleteToken() {
        String token = "testToken";
        doNothing().when(refreshTokenRepository).deleteByToken(token);
        jwtRefreshTokenService.deleteToken(token);
        verify(refreshTokenRepository, times(1)).deleteByToken(token);
    }
}
