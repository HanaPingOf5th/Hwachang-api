package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private String token;
    private String refreshToken;
}
