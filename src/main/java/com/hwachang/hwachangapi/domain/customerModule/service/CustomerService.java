package com.hwachang.hwachangapi.domain.customerModule.service;

import com.hwachang.hwachangapi.domain.clovaModule.service.ClovaApiService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.dto.*;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.database.AccountRole;
import com.hwachang.hwachangapi.utils.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final ClovaApiService clovaApiService;
    private final ConsultingRoomRepository consultingRoomRepository;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public String signup(CustomerSignupRequestDto request) {
        if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자 이름입니다.");
        }

        CustomerEntity customerEntity = CustomerEntity.create(
                request.getUsername(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                AccountRole.USER,
                request.getPhoneNumber()
        );
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

        String accessToken = jwtProvider.createAccessToken(customerEntity.getUsername(), customerEntity.getAccountRole());
        String refreshToken = jwtProvider.createRefreshToken(customerEntity.getUsername(), customerEntity.getName());

        return LoginResponseDto.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String token) {
        // 토큰 유효성 확인
        if (!jwtProvider.isTokenValid(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        // 로그아웃 처리 (서버에서는 추가 상태 관리를 하지 않음)
        System.out.println("로그아웃 성공: 클라이언트에서 토큰을 삭제하세요.");
    }

    @Transactional
    public List<ConsultingListDto> getCustomerConsultingRecords(UUID customerId) {
        // 1. 전체 상담방 조회
        List<ConsultingRoomEntity> allConsultingRooms = consultingRoomRepository.findAll();

        // 2. 특정 customerId가 포함된 상담방 필터링
        List<ConsultingRoomEntity> filteredRooms = allConsultingRooms.stream()
                .filter(room -> room.getCustomerIds() != null && room.getCustomerIds().contains(customerId))
                .collect(Collectors.toList());

        // 3. DTO 변환
        return filteredRooms.stream()
                .map(room -> {
                    // 상담을 담당한 행원 정보 조회
                    TellerEntity teller = tellerRepository.findById(room.getTellerId())
                            .orElseThrow(() -> new RuntimeException("담당 행원을 찾을 수 없습니다."));

                    // DTO 생성 및 반환
                    return ConsultingListDto.builder()
                            .consultingRoomId(room.getConsultingRoomId()) // 상담방 ID 추가
                            .summary(room.getSummary()) // 상담 요약
                            .tellerName(teller.getName()) // 행원 이름
                            .type(teller.getType().getDescription()) // 유형 (개인금융/기업금융)
                            .category(teller.getType().name()) // 카테고리 (enum key)
                            .date(room.getCreatedAt()) // 생성 날짜
                            .build();
                })
                .collect(Collectors.toList());
    }



    @Transactional
    public ConsultingDetailsDto getConsultingDetails(UUID customerId, UUID consultingRoomId) {
        // 1. 상담방 조회
        ConsultingRoomEntity consultingRoom = consultingRoomRepository.findById(consultingRoomId)
                .orElseThrow(() -> new RuntimeException("상담 방을 찾을 수 없습니다."));

        // 2. 고객 참여 검증
        if (consultingRoom.getCustomerIds() == null || !consultingRoom.getCustomerIds().contains(customerId)) {
            throw new RuntimeException("해당 고객은 이 상담에 참여하지 않았습니다.");
        }

        // 3. 행원 정보 조회
        TellerEntity teller = tellerRepository.findById(consultingRoom.getTellerId())
                .orElseThrow(() -> new RuntimeException("해당 상담을 담당한 행원을 찾을 수 없습니다."));

        // 4. DTO 생성 및 반환
        return ConsultingDetailsDto.builder()
                .summary(consultingRoom.getSummary())
                .originalText(consultingRoom.getOriginalText())
                .tellerName(teller.getName())
                .type(teller.getType().name())
                .category(teller.getType().getDescription())
                .date(consultingRoom.getCreatedAt())
                .build();
    }
}