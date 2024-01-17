package com.reddit.service.auth;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.model.dto.RefreshTokenRequest;
import com.reddit.model.dto.RegisterRequest;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import com.reddit.service.facade.FacadeRepositoryService;
import com.reddit.service.facade.LoginFacadeService;
import com.reddit.service.facade.SignupFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class JwtAuthService
        implements AuthService {

    private final FacadeRepositoryService facade;
    private final LoginFacadeService loginService;
    private final SignupFacadeService signupService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse login(
            LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @Override
    public ResponseEntity<String> signup(
            RegisterRequest request) {
        return signupService.signup(request);
    }

    @Override
    @Transactional
    public ResponseEntity<String> verifyAccount(String token) {
        return new ResponseEntity<>(
                "Account %s successfully verified"
                        .formatted(fetchUserAndEnable(
                                facade.findByToken(token))
                                           .getUsername()), OK);
    }

    @Override
    public User fetchUserAndEnable(VerificationToken token) {
        User user = fetchUser(token.getUser().getUsername());
        user.setEnabled(true);
        return facade.saveUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        return facade.getCurrentUser();
    }

    @Override
    public AuthenticationResponse refresh(
            RefreshTokenRequest request) {
        refreshTokenService.validateRefreshToken(
                request.getRefreshToken());
        return getAuthenticationResponse(request);
    }

    @Override
    public void deleteToken(String refreshToken) {
        refreshTokenService.deleteToken(refreshToken);
    }

    private User fetchUser(String username) {
        return facade.findByUsername(username);
    }

    private AuthenticationResponse getAuthenticationResponse(
            RefreshTokenRequest request) {
        return AuthenticationResponse
                .builder()
                .authenticationToken(getToken(request))
                .refreshToken(request.getRefreshToken())
                .expiresAt(loginService.getExpiration())
                .username(request.getUsername())
                .build();
    }

    private String getToken(RefreshTokenRequest request) {
        return refreshTokenService.generateRefreshToken(
                request.getUsername(),
                loginService.getExpiration()).getToken();
    }
}
