package com.hwachang.hwachangapi.domain.member.service.impl;

import com.hwachang.hwachangapi.domain.member.dto.Teller;
import com.hwachang.hwachangapi.domain.member.dto.TellerLoginRequestDto;
import com.hwachang.hwachangapi.domain.member.dto.TellerLoginResponseDto;
import com.hwachang.hwachangapi.domain.member.dto.TellerRegisterRequestDto;
import com.hwachang.hwachangapi.domain.member.entity.TellerEntity;
import com.hwachang.hwachangapi.domain.member.repository.MemberRepository;
import com.hwachang.hwachangapi.domain.member.service.TellerService;
import com.hwachang.hwachangapi.utils.randomGenerate.RandomCodeGenerator;
import com.hwachang.hwachangapi.utils.security.JwtProvider;
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
    private final JwtProvider jwtProvider;

    // ToDo: 아이디 중복 체크 등 멤버 비지니스 로직 추가
    @Override
    public Long registTeller(TellerRegisterRequestDto tellerRegisterRequestDto) {
        String salt = randomCodeGenerator.generateRandomCode();
        TellerEntity tellerEntity = TellerEntity.createTellerEntity(tellerRegisterRequestDto, salt);
        memberRepository.save(tellerEntity);
        return tellerEntity.getId();
    }

    @Override
    public TellerLoginResponseDto logInForTeller(TellerLoginRequestDto tellerLoginRequestDto) throws Exception {
        TellerEntity teller = (TellerEntity) memberRepository.findMemberByUsername(tellerLoginRequestDto.getUserName()).orElseThrow();
        if(!teller.getIsEnabled()){
            throw new Exception(teller.getTellerID() + " teller not verified");
        }

        String validationToken = jwtProvider.createValidationToken();

        teller.setValidationToken(validationToken);

        return TellerLoginResponseDto.builder().accessToken(jwtProvider.createAccessToken(teller.getUsername(), teller.getAccountRole()))
                .refreshToken(jwtProvider.createRefreshToken(teller.getUsername(), validationToken))
                .build();
    }
}
