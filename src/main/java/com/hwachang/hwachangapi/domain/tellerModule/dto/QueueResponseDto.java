package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueData.WaitingListCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class QueueResponseDto {
    private Map<Long, WaitingListCategory> waitingListCategory;
}