package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ConsultingRoomResponseDto {
    private UUID consultingRoom;
    private UUID categoryId;
    private UUID tellerId;
    private UUID customerId;
    private String userName;

    @JsonCreator
    public ConsultingRoomResponseDto(@JsonProperty("consultingRoom") UUID consultingRoom,
                                     @JsonProperty("categoryId") UUID categoryId,
                                     @JsonProperty("tellerId") UUID tellerId,
                                     @JsonProperty("customerId") UUID customerId,
                                     @JsonProperty("userName") String userName) {
        this.customerId = customerId;
        this.categoryId = categoryId;
        this.tellerId = tellerId;
        this.customerId = customerId;
        this.userName = userName;
    }
}
