package com.reddit.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token must not be blank")
    private String refreshToken;
    private String username;
}
