package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TellerStatusRequestDto {
    private String status;

    @JsonCreator
    public TellerStatusRequestDto(@JsonProperty("status") String status) {
        this.status = status;
    }
}