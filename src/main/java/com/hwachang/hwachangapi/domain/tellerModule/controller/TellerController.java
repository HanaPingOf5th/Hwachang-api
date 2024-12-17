package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.common.apiPayload.ApiResponse;
import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerInfoResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teller")
@RequiredArgsConstructor
public class TellerController {
    private final TellerService tellerService;

    @PostMapping("/signup")
    public String signUp(@RequestBody CreateTellerRequestDto createTellerRequestDto) {
        return this.tellerService.signup(createTellerRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto signIn(@RequestBody LoginRequestDto loginRequestDto) {
        return this.tellerService.login(loginRequestDto);
    }

    @GetMapping("/mypage")
    public ApiResponse<TellerInfoResponseDto> getTellerInfo() {
        TellerInfoResponseDto responseDto = tellerService.getTellerInfo();
        return ApiResponse.onSuccess(responseDto);
    }
}
