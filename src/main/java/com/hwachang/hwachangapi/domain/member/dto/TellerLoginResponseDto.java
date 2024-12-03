package com.hwachang.hwachangapi.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TellerLoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
