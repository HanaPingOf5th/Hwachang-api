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
    private String tellerName;
    private UUID customerId;
    private String customerName;

    @JsonCreator
    public ConsultingRoomResponseDto(@JsonProperty("consultingRoomId") UUID consultingRoomId,
                                     @JsonProperty("categoryId") UUID categoryId,
                                     @JsonProperty("tellerId") UUID tellerId,
                                     @JsonProperty("tellerName") String tellerName,
                                     @JsonProperty("customerId") UUID customerId,
                                     @JsonProperty("customerName") String customerName) {
        this.consultingRoomId = consultingRoomId;
        this.categoryId = categoryId;
        this.tellerId = tellerId;
        this.tellerName = tellerName;
        this.customerId = customerId;
        this.customerName = customerName;
    }
}
