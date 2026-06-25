package com.ironhack.trelloforween.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
