package com.hwachang.hwachangapi.domain.tellerModule.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueueResponseDto {
    // 고객대기중, 대기, 통화중, 후처리에 해당하는 고객 및 행원 수
    private Long waitingCustomer;
    private Long waitingTeller;
    private Long calling;
    private Long postProcessing;
}