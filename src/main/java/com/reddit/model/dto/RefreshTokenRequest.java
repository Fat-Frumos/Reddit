package com.reddit.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
    private String username;
}
