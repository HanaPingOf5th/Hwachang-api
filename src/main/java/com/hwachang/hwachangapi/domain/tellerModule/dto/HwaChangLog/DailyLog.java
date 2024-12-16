package com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DailyLog {
    public List<Integer> today;
    public List<Integer> yesterday;
}
