package com.reddit.service.auth;

import com.reddit.exception.SpringRedditException;
import com.reddit.model.dto.RefreshToken;
import com.reddit.repository.RefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.KeyStore;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtRefreshTokenService
        implements RefreshTokenService {
    private KeyStore keyStore;
    private static final String SECRET = "bootsecurity";

    private final RefreshTokenRepository refreshTokenRepository;
    private static final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);

    @PostConstruct
    public void  init(){
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(getClass().getResourceAsStream("/bootsecurity.p12"), SECRET.toCharArray());
        } catch (Exception e) {
            throw new SpringRedditException("PostConstruct failed");
        }
    }

    @Override
    public RefreshToken generateRefreshToken(String username, Instant millis) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(generateToken(username, millis));
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(String token) {
        refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
    }

    @Override
    public void deleteToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public String generateToken(String username, Instant expiryTime) {
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(Date.from(Instant.now()))
                   .signWith(keyPair.getPrivate(), SignatureAlgorithm.ES256)
                   .setExpiration(Date.from(expiryTime))
                   .compact();
    }
}
