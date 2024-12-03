package com.hwachang.hwachangapi.domain.member.controller;

import com.hwachang.hwachangapi.domain.member.dto.Teller;
import com.hwachang.hwachangapi.domain.member.dto.TellerRegisterRequestDto;
import com.hwachang.hwachangapi.domain.member.mapper.TellerServiceMapper;
import com.hwachang.hwachangapi.utils.annotations.DisableSwaggerSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
