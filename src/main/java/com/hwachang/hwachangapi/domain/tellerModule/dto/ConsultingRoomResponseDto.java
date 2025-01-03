package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ConsultingRoomResponseDto {
    private UUID consultingRoomId;
    private UUID categoryId;
    private UUID tellerId;
    private UUID customerId;
    private String userName;

    @JsonCreator
    public ConsultingRoomResponseDto(@JsonProperty("consultingRoomId") UUID consultingRoomId,
                                     @JsonProperty("categoryId") UUID categoryId,
                                     @JsonProperty("tellerId") UUID tellerId,
                                     @JsonProperty("customerId") UUID customerId,
                                     @JsonProperty("userName") String userName) {
        this.consultingRoomId = consultingRoomId;
        this.categoryId = categoryId;
        this.tellerId = tellerId;
        this.customerId = customerId;
        this.userName = userName;
    }
}
