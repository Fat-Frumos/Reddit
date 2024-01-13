package com.epam.esm.reddit.security;

import com.epam.esm.reddit.exception.SpringRedditException;
import com.epam.esm.reddit.model.entity.User;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.Signature;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JWTProvider {

    @Value(value = "${jwt.expiration.time}")
    private String expiration;

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return generateTokenWithUserName(principal.getUsername());
    }

    public String generateTokenWithUserName(String username) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(getJwtExpirationInMillis()))
                .subject(username)
                .claim("scope", "ROLE_USER")
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            PublicKey publicKey = getPublicKey();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update((parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getUrlDecoder().decode(parts[2]));
        } catch (Exception e) {
            throw new SpringRedditException("Exception", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            return KeyStore.getInstance("RS256").getCertificate("").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception", e);
        }
    }

    public String getUsernameFromJwt(String token) {
        String[] parts = token.split("\\.");
        byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
        String payload = new String(payloadBytes, StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();
        return jsonObject.get("username").getAsString();
    }

    public Long getJwtExpirationInMillis() {
        return Long.valueOf(expiration);
    }
}
