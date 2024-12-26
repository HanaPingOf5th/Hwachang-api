package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SendApplicationFormDto {
    private String username;
    private UUID formId;
}
