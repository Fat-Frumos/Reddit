package com.epam.esm.reddit.service.facade;

import com.epam.esm.reddit.model.dto.AuthenticationResponse;
import com.epam.esm.reddit.model.dto.LoginRequest;

public interface LoginService {
    AuthenticationResponse login(LoginRequest request);
}
