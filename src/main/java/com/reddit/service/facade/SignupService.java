package com.reddit.service.facade;

import com.reddit.model.dto.RegisterRequest;
import com.reddit.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface SignupService {
    @Transactional
    ResponseEntity<String> signup(RegisterRequest request);

    String createVerificationToken(User user);
}
