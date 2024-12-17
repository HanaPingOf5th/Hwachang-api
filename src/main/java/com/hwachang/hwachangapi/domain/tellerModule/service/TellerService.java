package com.hwachang.hwachangapi.domain.tellerModule.service;

import com.hwachang.hwachangapi.common.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerInfoResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.AccountRole;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Status;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        System.out.println("---------------");
        System.out.println(request.getPassword());
        TellerEntity tellerEntity = TellerEntity.builder()
                .userName(request.getTellerNumber())
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
        String accessToken = jwtProvider.createAccessToken(String.valueOf(tellerEntity.getId()), tellerEntity.getName());
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(tellerEntity.getId()), tellerEntity.getName());

        return LoginResponseDto.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 행원 정보 조회
    public TellerInfoResponseDto getTellerInfo() {
        System.out.println("getTellerInfo 호출");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        TellerEntity teller = tellerRepository.findTellerByUserName(username)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        return TellerInfoResponseDto.builder()
                .name(teller.getName())
                .position(teller.getPosition())
                .status(teller.getStatus().getDescription())
                .type(teller.getType().getDescription())
                .build();
    }
}
