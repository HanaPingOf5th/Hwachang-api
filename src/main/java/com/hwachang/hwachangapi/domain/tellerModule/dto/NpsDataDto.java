package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class NpsDataDto {
    private Long promoter;
    private Long neutral;
    private Long detractor;

    public NpsDataDto(Long promoter, Long neutral, Long detractor) {
        this.promoter = promoter;
        this.neutral = neutral;
        this.detractor = detractor;
    }
}
