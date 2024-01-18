package com.reddit.service.auth;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;
import com.reddit.model.dto.RefreshToken;
import com.reddit.model.dto.RefreshTokenRequest;
import com.reddit.model.dto.RegisterRequest;
import com.reddit.model.entity.User;
import com.reddit.model.entity.VerificationToken;
import com.reddit.service.facade.FacadeRepositoryService;
import com.reddit.service.facade.LoginFacadeService;
import com.reddit.service.facade.SignupFacadeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class JwtAuthServiceTest {

    @Mock
    private FacadeRepositoryService facade;
    @Mock
    private LoginFacadeService loginService;
    @Mock
    private SignupFacadeService signupService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @InjectMocks
    private JwtAuthService jwtAuthService;

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        AuthenticationResponse response = new AuthenticationResponse();
        when(loginService.login(loginRequest)).thenReturn(response);
        assertEquals(response, jwtAuthService.login(loginRequest));
    }

    @Test
    void testSignup() {
        RegisterRequest request = new RegisterRequest();
        ResponseEntity<String> response = ResponseEntity.ok("Success");
        when(signupService.signup(request)).thenReturn(response);
        assertEquals(response, jwtAuthService.signup(request));
    }

//    @Test
    @Transactional
    void testVerifyAccount() {
        String token = "yourToken";
        User mockUser = new User();
        mockUser.setUsername("mockUsername");
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(mockUser);
        verificationToken.setToken(token);
        when(facade.findByToken(token)).thenReturn(verificationToken);
        when(jwtAuthService.fetchUserAndEnable(any())).thenAnswer(invocation -> {
            VerificationToken tokenArgument = invocation.getArgument(0);
            User user = new User();
            user.setUsername(tokenArgument.getUser().getUsername());
            user.setEnabled(true);
            return user;
        });

        when(facade.saveUser(any())).thenAnswer(invocation -> {
            User userArgument = invocation.getArgument(0);
            userArgument.setEnabled(true);
            return userArgument;
        });

        ResponseEntity<String> responseEntity = jwtAuthService.verifyAccount(token);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Account mockUsername successfully verified", responseEntity.getBody());
    }

    @Test
    void testFetchUserAndEnable() {
        User user = new User();
        user.setUsername("testUser");
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        when(facade.findByUsername(user.getUsername())).thenReturn(user);
        when(facade.saveUser(user)).thenReturn(user);
        User result = jwtAuthService.fetchUserAndEnable(token);
        assertTrue(result.isEnabled());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        user.setUsername("testUser");
        when(facade.getCurrentUser()).thenReturn(user);
        User result = jwtAuthService.getCurrentUser();
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void testRefresh() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("testRefreshToken");
        request.setUsername("testUser");
        doNothing().when(refreshTokenService).validateRefreshToken(request.getRefreshToken());
        when(loginService.getExpiration()).thenReturn(Instant.now().plusSeconds(900));
        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken(UUID.randomUUID().toString());
        when(refreshTokenService.generateRefreshToken(
                request.getUsername(), loginService.getExpiration()))
                .thenReturn(mockRefreshToken);
        AuthenticationResponse result = jwtAuthService.refresh(request);
        assertNotNull(result);
    }

    @Test
    void testDeleteToken() {
        String refreshToken = "testRefreshToken";
        doNothing().when(refreshTokenService).deleteToken(refreshToken);
        jwtAuthService.deleteToken(refreshToken);
        verify(refreshTokenService, times(1)).deleteToken(refreshToken);
    }
}
