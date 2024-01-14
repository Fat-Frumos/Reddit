package com.reddit.security;

import com.reddit.exception.SpringRedditException;
import com.nimbusds.jose.shaded.gson.JsonParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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
        try {
            String[] parts = jwt.split("\\.");
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(getPublicKey());
            signature.update((parts[0] + "." + parts[1]).getBytes(
                    UTF_8));
            return signature.verify(Base64.getUrlDecoder().decode(parts[2]));
        } catch (Exception e) {
            throw new SpringRedditException("Exception", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            return KeyStore.getInstance("RS256")
                           .getCertificate("")
                           .getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception", e);
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
