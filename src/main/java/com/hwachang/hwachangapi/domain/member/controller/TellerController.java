package com.hwachang.hwachangapi.domain.member.controller;

import com.hwachang.hwachangapi.domain.member.dto.Teller;
import com.hwachang.hwachangapi.domain.member.dto.TellerLoginRequestDto;
import com.hwachang.hwachangapi.domain.member.dto.TellerLoginResponseDto;
import com.hwachang.hwachangapi.domain.member.dto.TellerRegisterRequestDto;
import com.hwachang.hwachangapi.domain.member.mapper.TellerServiceMapper;
import com.hwachang.hwachangapi.utils.annotations.DisableSwaggerSecurity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/teller")
@RequiredArgsConstructor
public class TellerController {
    private final TellerServiceMapper tellerServiceMapper;

    @PostMapping
    @DisableSwaggerSecurity
    public Long tellerRegister(@RequestBody TellerRegisterRequestDto request){
        return tellerServiceMapper.get(1L).registTeller(request);
    }

    @PostMapping("/login")
    public TellerLoginResponseDto loginForTeller(@RequestBody TellerLoginRequestDto request) throws Exception {
        return tellerServiceMapper.get(1L).logInForTeller(request);
    }
}
