package com.hwachang.hwachangapi.domain.tellerModule.service;

import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.AccountRole;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Status;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TellerService {
    private final TellerRepository tellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(CreateTellerRequestDto request) {
        TellerEntity tellerEntity = TellerEntity.builder()
                .username(request.getTellerNumbers())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .type(Type.CORPORATE)
                .status(Status.AVAILABLE)
                .accountRole(AccountRole.Teller)
                .position(request.getPosition()).build();

        this.tellerRepository.save(tellerEntity);
        return tellerEntity.getUsername();
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        // 행원 조회
        TellerEntity tellerEntity = this.tellerRepository.findTellerByUserName(loginRequestDto.getTellerNumber())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), tellerEntity.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }

        // Access Token 및 Refresh Token 생성
        String accessToken = jwtProvider.createAccessToken(tellerEntity.getUsername(), tellerEntity.getAccountRole());
        String refreshToken = jwtProvider.createRefreshToken(tellerEntity.getUsername(), tellerEntity.getName());

        return LoginResponseDto.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
