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
        try{
            TellerEntity tellerEntity = this.tellerRepository.findTellerByUserName(loginRequestDto.getTellerNumber()).orElseThrow();
            String password = passwordEncoder.encode(loginRequestDto.getPassword());

            if(passwordEncoder.matches(loginRequestDto.getPassword(), password)){
                String accessToken = jwtProvider.createAccessToken(String.valueOf(tellerEntity.getId()), tellerEntity.getName());
                String refreshToken = jwtProvider.createRefreshToken(String.valueOf(tellerEntity.getId()), tellerEntity.getName());
                return LoginResponseDto.builder().token(accessToken).refreshToken(refreshToken).build();
            }else {
                // ToDo: 커스텀 예외 처리 필요
                throw new RuntimeException("비밀번호가 맞지 않습니다.");
            }

        }catch (Exception e){
            if(e.getMessage().equals("비밀번호가 맞지 않습니다.")){
                throw new RuntimeException("비밀번호가 맞지 않습니다.");
            }else {
                throw new RuntimeException(e);
            }
        }
    }
}
