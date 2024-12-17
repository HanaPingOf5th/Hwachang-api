package com.hwachang.hwachangapi.domain.customerModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.dto.ConsultingListDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.LoginRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.LoginResponseDto;
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

    private final RestTemplate restTemplate = new RestTemplate();

    private final ConsultingRoomRepository consultingRoomRepository;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public String callClovaApi(String userMessage) {
        // Clova Studio API 호출을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", "NTA0MjU2MWZlZTcxNDJiY97QrrA6UMhe0PTH7P9CpKmtMwqOVj8p1U5/OhclIE6b"); // 발급 받은 API KEY
        headers.set("X-NCP-APIGW-API-KEY", "y6bvnqr0gP7MpGAyp4GBOVBPaIQZg9nVoAARj8DD"); // 발급 받은 API Gateway KEY
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // 요청 Body 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("topP", 0.8);
        requestBody.put("topK", 0);
        requestBody.put("maxTokens", 500);
        requestBody.put("temperature", 0.5);
        requestBody.put("repeatPenalty", 5.0);
        requestBody.put("stopBefore", List.of());
        requestBody.put("includeAiFilters", true);
        requestBody.put("seed", 0);

        // 대화 메시지 설정
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);
        requestBody.put("messages", List.of(message));

        // API 호출
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://clovastudio.stream.ntruss.com/testapp/v1/chat-completions/HCX-003",
                requestEntity,
                Map.class
        );

        // 결과 반환
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> result = (Map<String, Object>) responseBody.get("result");
        Map<String, Object> messageResult = (Map<String, Object>) result.get("message");

        return (String) messageResult.get("content");
    }

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
        // 손님의 UUID를 기준으로 상담방 검색
        List<ConsultingRoomEntity> consultingRooms = consultingRoomRepository.findByCustomerId(customerId);

        return consultingRooms.stream()
                .map(room -> {
                    // 상담을 담당한 행원 정보 조회
                    TellerEntity teller = tellerRepository.findById(room.getBankerId())
                            .orElseThrow(() -> new RuntimeException("담당 행원을 찾을 수 없습니다."));

                    // DTO 변환
                    return ConsultingListDto.builder()
                            .summary(room.getSummary()) // 상담 요약
                            .tellerName(teller.getName()) // 행원 이름
                            .type(teller.getType().getDescription()) // 유형 (개인금융/기업금융)
                            .category(teller.getType().name()) // 카테고리 (enum key)
                            .date(room.getCreatedAt()) // 생성 날짜
                            .build();
                })
                .collect(Collectors.toList());
    }
}