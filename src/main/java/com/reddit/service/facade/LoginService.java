package com.reddit.service.facade;

import com.reddit.model.dto.AuthenticationResponse;
import com.reddit.model.dto.LoginRequest;

public interface LoginService {
    AuthenticationResponse login(LoginRequest request);
}
