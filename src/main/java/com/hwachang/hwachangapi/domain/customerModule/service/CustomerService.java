package com.hwachang.hwachangapi.domain.customerModule.service;

import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.LoginResponseDto;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.utils.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(CustomerSignupRequestDto request) {
        if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자 이름입니다.");
        }

        CustomerEntity customerEntity = CustomerEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        customerRepository.save(customerEntity);
        return customerEntity.getUsername();
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        CustomerEntity customerEntity = customerRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), customerEntity.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(String.valueOf(customerEntity.getId()), customerEntity.getName());
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(customerEntity.getId()), customerEntity.getName());

        return LoginResponseDto.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}