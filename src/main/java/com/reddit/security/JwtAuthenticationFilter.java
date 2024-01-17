package com.reddit.security;

import com.nimbusds.jose.shaded.gson.JsonParser;
import com.reddit.exception.SpringRedditException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${server.ssl.key-store-type}")
    private String keyStoreType;

    @Value("${server.ssl.key-alias}")
    private String keyAlias;

    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${server.ssl.key-store}")
    private String keyStorePath;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        if (StringUtils.hasText(jwt) && validateToken(jwt)) {
            String username = getUsernameFromJwt(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    public boolean validateToken(String jwt) {
        if (jwt == null || jwt.isEmpty()) {
            throw new SpringRedditException("JWT token is null or empty");
        }

        try {
            String[] parts = jwt.split("\\.");
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(getPublicKey());
            signature.update((parts[0] + "." + parts[1]).getBytes(UTF_8));
            return signature.verify(Base64.getUrlDecoder().decode(parts[2]));
        } catch (Exception e) {
            throw new SpringRedditException("invalidate Token");
        }
    }

    private PublicKey getPublicKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(getClass().getResourceAsStream("/bootsecurity.p12"), keyStorePassword.toCharArray());
            Certificate certificate = keyStore.getCertificate(keyAlias);
            if (certificate == null) {
                throw new SpringRedditException("Certificate not found for alias: " + keyAlias);
            }

            return certificate.getPublicKey();
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new SpringRedditException("invalid PublicKey");
        }
    }

    public String getUsernameFromJwt(String token) {
        return JsonParser.parseString(new String(
                Base64.getUrlDecoder().decode(
                        token.split("\\.")[1]), UTF_8))
                         .getAsJsonObject()
                         .get("username")
                         .getAsString();
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        return StringUtils.hasText(authHeader)
                && authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
    }
}
