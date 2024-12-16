package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTellerRequestDto {
    private String tellerNumber;
    private String name;
    private String password;
    private String position;
}
