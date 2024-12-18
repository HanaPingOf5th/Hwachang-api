package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.LogData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TellerReviewResponse {
    private List<String> reviews;
}