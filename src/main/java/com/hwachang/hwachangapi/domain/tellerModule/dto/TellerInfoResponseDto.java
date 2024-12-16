package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TellerInfoResponseDto {
    private String name;
    private String position;
    private String type;
    private String status;
}
