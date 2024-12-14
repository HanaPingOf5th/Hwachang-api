package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.hwachang.hwachangapi.domain.tellerModule.entities.AccountRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTellerRequestDto {
    private String tellerNumber;
    private String name;
    private String password;
    private String position;
    private String status;
    private String type;
}
