package com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog;

import lombok.Data;

import java.util.List;

@Data
public class DailyLog {
    public List<Integer> today;
    public List<Integer> yesterday;
}
