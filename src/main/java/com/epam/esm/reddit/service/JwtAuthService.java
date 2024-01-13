package com.epam.esm.reddit.service;

import com.epam.esm.reddit.model.dto.AuthenticationResponse;
import com.epam.esm.reddit.model.dto.LoginRequest;
import com.epam.esm.reddit.model.dto.RegisterRequest;
import com.epam.esm.reddit.model.entity.User;
import com.epam.esm.reddit.model.entity.VerificationToken;
import com.epam.esm.reddit.service.facade.FacadeRepositoryService;
import com.epam.esm.reddit.service.facade.LoginFacadeService;
import com.epam.esm.reddit.service.facade.SignupFacadeService;
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
    public ResponseEntity<String> signup(RegisterRequest request) {
        return signupService.signUp(request);
    }

    @Override
    public ResponseEntity<String> verifyAccount(String token) {
        User user = fetchUserAndEnable(repository.findByToken(token));
        return new ResponseEntity<>("Account %s successfully verified".formatted(
                user.getUsername()), OK);
    }


    @Override
    public User fetchUserAndEnable(VerificationToken token) {
        String username = token.getUser().getUsername();
        User user = fetchUser(username);
        user.setEnabled(true);
        return repository.saveUser(user);
    }


    private User fetchUser(String username) {
        return repository.findByUsername(username);
    }
}
