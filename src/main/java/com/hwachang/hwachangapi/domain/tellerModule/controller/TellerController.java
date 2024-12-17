package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.utils.apiPayload.ApiResponse;
import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerInfoResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teller")
@RequiredArgsConstructor
public class TellerController {
    private static final Logger log = LogManager.getLogger(TellerController.class);
    private final TellerService tellerService;

    @PostMapping("/signup")
    public String signUp(@RequestBody CreateTellerRequestDto createTellerRequestDto) {
        log.info("행원 회원가입 " + createTellerRequestDto.getName());
        return this.tellerService.signup(createTellerRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto signIn(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("행원 로그인 " + loginRequestDto.getTellerNumber());
        return this.tellerService.login(loginRequestDto);
    }

    @GetMapping("/mypage")
    public ApiResponse<TellerInfoResponseDto> getTellerInfo() {
        TellerInfoResponseDto responseDto = tellerService.getTellerInfo();
        return ApiResponse.onSuccess(responseDto);
    }
}
