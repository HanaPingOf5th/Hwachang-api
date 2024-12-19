package com.hwachang.hwachangapi.domain.tellerModule.dto.QueueData;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WaitingListCategory {
    // 고객대기중, 대기, 통화중, 후처리에 해당하는 고객 및 행원 수
    private Integer waitingCustomer;
    private Integer waitingTeller;
    private Integer calling;
    private Integer postProcessing;
}
