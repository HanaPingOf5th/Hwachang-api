package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.domain.tellerModule.dto.HwaChangLog.LogData;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/teller")
@RequiredArgsConstructor
public class HwaChangLogController {
    private final TellerService tellerService;

//    @GetMapping("/graph")
//    public LogData getGraphData() {
//
//
//    }
}
