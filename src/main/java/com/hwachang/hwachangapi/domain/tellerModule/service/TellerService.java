package com.hwachang.hwachangapi.domain.tellerModule.service;

import com.hwachang.hwachangapi.domain.tellerModule.dto.*;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import com.hwachang.hwachangapi.utils.apiPayload.exception.InvalidStatusException;
import com.hwachang.hwachangapi.utils.apiPayload.exception.handler.UserHandler;
import com.hwachang.hwachangapi.utils.database.AccountRole;
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
        TellerEntity tellerEntity = TellerEntity.create(
                request.getTellerNumber(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                AccountRole.Teller,
                request.getPosition(),
                Status.AVAILABLE,
                Type.CORPORATE,
                "profile-image-url"
        );

        this.tellerRepository.save(tellerEntity);
        return tellerEntity.getUsername();
    }

    @Transactional
    public String signupP(CreateTellerRequestDto request) {
        TellerEntity tellerEntity = TellerEntity.create(
                request.getTellerNumber(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                AccountRole.Teller,
                request.getPosition(),
                Status.AVAILABLE,
                Type.PERSONAL,
                "profile-image-url"
        );

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

    // 행원 정보 조회
    public TellerInfoResponseDto getTellerInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        TellerEntity teller = tellerRepository.findTellerByUserName(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.TELLER_NOT_FOUND));

        return TellerInfoResponseDto.builder()
                .name(teller.getName())
                .position(teller.getPosition())
                .status(teller.getStatus().getDescription())
                .type(teller.getType().getDescription())
                .build();
    }

    // 행원 상태 변경
    @Transactional
    public void updateStatus(TellerStatusRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        TellerEntity teller = tellerRepository.findTellerByUserName(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.TELLER_NOT_FOUND));

        try {
            teller.changeStatus(Status.valueOf(requestDto.getStatus()));
            tellerRepository.save(teller);
        } catch (InvalidStatusException e) {
            System.err.println("Invalid status: " + e.getMessage());
            throw new UserHandler(ErrorStatus.INVALID_STATUS);
        }
    }
}
