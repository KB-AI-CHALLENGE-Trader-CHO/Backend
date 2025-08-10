package com.kbai.tradejoe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JwtTokenDto extends SelfValidating<JwtTokenDto> {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.validateSelf();
    }
}
