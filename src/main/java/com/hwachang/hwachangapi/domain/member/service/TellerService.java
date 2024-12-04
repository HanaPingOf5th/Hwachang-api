package com.hwachang.hwachangapi.domain.member.service;

import com.hwachang.hwachangapi.domain.member.dto.TellerLoginResponseDto;
import com.hwachang.hwachangapi.domain.member.dto.TellerRegisterRequestDto;
import com.hwachang.hwachangapi.domain.member.dto.Teller;
import com.hwachang.hwachangapi.domain.member.dto.TellerLoginRequestDto;

public interface TellerService {
    Long registTeller(TellerRegisterRequestDto tellerRegisterRequestDto);
    TellerLoginResponseDto logInForTeller(TellerLoginRequestDto tellerLoginRequestDto) throws Exception;
}
