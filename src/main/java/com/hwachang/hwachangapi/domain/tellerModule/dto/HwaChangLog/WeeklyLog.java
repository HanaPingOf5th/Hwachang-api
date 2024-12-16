package com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog;

import lombok.Data;

import java.util.List;

@Data
public class WeeklyLog {
    public List<Integer> thisWeek;
    public List<Integer> lastWeek;
}