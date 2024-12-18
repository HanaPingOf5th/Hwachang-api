package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TellerReviewResponseDto {
    private List<String> reviews;
}