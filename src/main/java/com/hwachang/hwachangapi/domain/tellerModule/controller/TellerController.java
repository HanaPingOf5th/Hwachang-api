package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
