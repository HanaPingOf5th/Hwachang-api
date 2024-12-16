package com.hwachang.hwachangapi.domain.tellerModule.dto.QueueData;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryData {
    // 고객대기중, 대기, 통화중, 후처리, 이석에 해당하는 고객 수
    private Integer waitingCustomer;
    private Integer waitingTeller;
    private Integer calling;
    private Integer postProcessing;
    private Integer leave;
}
