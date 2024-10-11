package com.duongw.sbsecurity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    private String message;

    // some attribute: platform, device code, country code
}
