package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {
    private String tellerNumber;
    private String password;
}
