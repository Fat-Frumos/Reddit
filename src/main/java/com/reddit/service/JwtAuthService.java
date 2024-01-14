package com.reddit.service;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.model.dto.RegisterRequest;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import com.reddit.service.facade.FacadeRepositoryService;
import com.reddit.service.facade.LoginFacadeService;
import com.reddit.service.facade.SignupFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class JwtAuthService
        implements AuthService {

    private final FacadeRepositoryService repository;
    private final SignupFacadeService signupService;
    private final LoginFacadeService loginService;

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
    public ResponseEntity<String> verifyAccount(String token) {
        User user = fetchUserAndEnable(repository.findByToken(token));
        return new ResponseEntity<>("Account %s successfully verified".formatted(
                user.getUsername()), OK);
    }


    @Override
    public User fetchUserAndEnable(VerificationToken token) {
        User user = fetchUser(token.getUser().getUsername());
        user.setEnabled(true);
        return repository.saveUser(user);
    }


    private User fetchUser(String username) {
        return repository.findByUsername(username);
    }
}
