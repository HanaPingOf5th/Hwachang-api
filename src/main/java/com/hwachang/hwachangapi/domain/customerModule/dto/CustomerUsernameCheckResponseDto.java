package com.hwachang.hwachangapi.domain.customerModule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerUsernameCheckResponseDto {
    private boolean isAvailable;
    private String message;
}