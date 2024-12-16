package com.hwachang.hwachangapi.domain.customerModule.controller;

import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.customerModule.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/signup")
    public String signUp(@RequestBody CustomerSignupRequestDto customerSignupRequestDto) {
        return this.customerService.signup(customerSignupRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto signIn(@RequestBody LoginRequestDto loginRequestDto) {
        return this.customerService.login(loginRequestDto);
    }
}