package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ConsultingRoomResponseDto {
    private UUID consultingRoom;
    private UUID categoryId;
    private UUID customerId;
    private String userName;
}
