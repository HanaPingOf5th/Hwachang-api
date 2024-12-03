package com.hwachang.hwachangapi.domain.member.service.impl;

import com.hwachang.hwachangapi.domain.member.dto.Teller;
import com.hwachang.hwachangapi.domain.member.dto.TellerLoginRequestDto;
import com.hwachang.hwachangapi.domain.member.dto.TellerRegisterRequestDto;
import com.hwachang.hwachangapi.domain.member.entity.TellerEntity;
import com.hwachang.hwachangapi.domain.member.repository.MemberRepository;
import com.hwachang.hwachangapi.domain.member.service.TellerService;
import com.hwachang.hwachangapi.utils.randomGenerate.RandomCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TellerServiceImplV1 implements TellerService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomCodeGenerator randomCodeGenerator;

    @Override
    public Long registTeller(TellerRegisterRequestDto tellerRegisterRequestDto) throws Error {
        String salt = randomCodeGenerator.generateRandomCode();
        TellerEntity tellerEntity = TellerEntity.createTellerEntity(tellerRegisterRequestDto, salt);
        memberRepository.save(tellerEntity);
        return tellerEntity.getId();
    }

    @Override
    public Long logInForTeller(TellerLoginRequestDto tellerLoginRequestDto) {
        return null;
    }
}
