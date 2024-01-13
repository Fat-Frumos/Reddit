package com.epam.esm.reddit.service.facade;

import com.epam.esm.reddit.model.dto.RegisterRequest;
import com.epam.esm.reddit.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface SignupService {
    @Transactional
    ResponseEntity<String> signUp(RegisterRequest request);

    String createVerificationToken(User user);
}
