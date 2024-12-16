package com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog;

import lombok.Data;

import java.util.List;

@Data
public class MonthlyLog{
    public List<Integer> thisMonth;
    public List<Integer> lastMonth;
}