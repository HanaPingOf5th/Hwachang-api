package com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogData {
    private DailyLog dailyLog;
    private WeeklyLog weeklyLog;
    private MonthlyLog monthlyLog;
}
