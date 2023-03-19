package com.epam.esm.reddit.security;

import com.epam.esm.reddit.domain.Person;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

@Service
@AllArgsConstructor
public class JWTProvider {

    private final JwtEncoder jwtEncoder;
    @Value("${jwt.expiration.time}")
    private final Long jwtExpirationInMillis;

    public String generateToken(Authentication authentication) {
        Person principal = (Person) authentication.getPrincipal();
        return generateTokenWithUsername(principal.getUsername());
    }

    private String generateTokenWithUsername(String username) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now()
                        .plusMillis(jwtExpirationInMillis))
                .subject(username)
                .claim("scope", "ROLE_USER")
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
