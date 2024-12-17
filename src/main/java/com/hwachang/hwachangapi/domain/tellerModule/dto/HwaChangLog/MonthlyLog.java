package com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MonthlyLog{
    public List<Integer> thisMonth;
    public List<Integer> lastMonth;
}