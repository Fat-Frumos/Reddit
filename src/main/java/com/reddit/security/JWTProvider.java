package com.reddit.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JWTProvider {

    @Value(value = "${jwt.expiration.time}")
    private Long expiration;

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return generateTokenWithUserName(userPrincipal.getUsername());
        } else {
            throw new IllegalStateException("Unexpected principal type found in Authentication");
        }
    }

    public String generateTokenWithUserName(String username) {
        JwtClaimsSet claims = getClaims(username);
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @NonNull
    private JwtClaimsSet getClaims(String username) {
        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(expiration))
                .subject(username)
                .claim("scope", "ROLE_USER")
                .build();}

}
