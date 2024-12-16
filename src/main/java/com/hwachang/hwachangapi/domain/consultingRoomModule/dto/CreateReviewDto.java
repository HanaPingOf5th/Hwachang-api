package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CreateReviewDto {
    private UUID customerId;

    private Integer nps;

    private String content;

    private UUID consultingRoomId;
}
