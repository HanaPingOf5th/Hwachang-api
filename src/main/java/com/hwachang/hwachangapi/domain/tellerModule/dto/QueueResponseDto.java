package com.hwachang.hwachangapi.domain.tellerModule.dto;

import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueData.CategoryData;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class QueueResponseDto {
    private Map<String, CategoryData> userList;
}