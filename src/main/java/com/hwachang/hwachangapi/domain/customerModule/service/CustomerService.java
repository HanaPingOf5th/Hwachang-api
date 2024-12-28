package com.hwachang.hwachangapi.domain.customerModule.service;

import com.hwachang.hwachangapi.domain.clovaModule.service.ClovaApiService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private final CategoryRepository categoryRepository;


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


    public UUID getCustomerId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        CustomerEntity customerEntity = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return customerEntity.getId();
    }

    // 로그인된 고객의 상담 기록 목록 가져오기
    @Transactional
    public List<ConsultingListDto> getCustomerConsultingRecords(String summaryKeyword, String startDate, String endDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        CustomerEntity customerEntity = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.MIN;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.MAX;

        List<ConsultingRoomEntity> allConsultingRooms = consultingRoomRepository.findAll();

        List<ConsultingRoomEntity> filteredRooms = allConsultingRooms.stream()
                .filter(room -> room.getCustomerIds() != null && room.getCustomerIds().contains(customerEntity.getId()))
                .filter(room -> {
                    LocalDate roomDate = room.getCreatedAt().toLocalDate();
                    return !roomDate.isBefore(start) && !roomDate.isAfter(end);
                })
                .filter(room -> summaryKeyword == null || room.getSummary().contains(summaryKeyword))
                .collect(Collectors.toList());

        return filteredRooms.stream()
                .map(room -> {
                    TellerEntity teller = tellerRepository.findById(room.getTellerId())
                            .orElseThrow(() -> new RuntimeException("담당 행원을 찾을 수 없습니다."));

                    CategoryEntity category = categoryRepository.findById(room.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));

                    return ConsultingListDto.builder()
                            .consultingRoomId(room.getConsultingRoomId())
                            .summary(room.getSummary())
                            .tellerName(teller.getName())
                            .type(teller.getType().getDescription())
                            .category(category.getCategoryName())
                            .date(room.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 로그인된 고객의 특정 상담 상세 정보 가져오기
    @Transactional
    public ConsultingDetailsDto getConsultingDetails(UUID consultingRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        CustomerEntity customerEntity = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ConsultingRoomEntity consultingRoom = consultingRoomRepository.findById(consultingRoomId)
                .orElseThrow(() -> new RuntimeException("상담 방을 찾을 수 없습니다."));

        if (consultingRoom.getCustomerIds() == null || !consultingRoom.getCustomerIds().contains(customerEntity.getId())) {
            throw new RuntimeException("해당 고객은 이 상담에 참여하지 않았습니다.");
        }

        TellerEntity teller = tellerRepository.findById(consultingRoom.getTellerId())
                .orElseThrow(() -> new RuntimeException("담당 행원을 찾을 수 없습니다."));

        CategoryEntity category = categoryRepository.findById(consultingRoom.getCategoryId())
                .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));

        return ConsultingDetailsDto.builder()
                .summary(consultingRoom.getSummary())
                .originalText(consultingRoom.getOriginalText())
                .tellerName(teller.getName())
                .type(teller.getType().name())
                .category(category.getCategoryName())
                .date(consultingRoom.getCreatedAt())
                .voiceUrl(consultingRoom.getVoiceRecordUrl())
                .build();
    }

    public UserInfoDto getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Optional<CustomerEntity> customer = customerRepository.findByUsername(username);

        if (customer.isPresent()) {
            return UserInfoDto.builder()
                    .name(customer.get().getName())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }

    }

    @Transactional
    public CustomerUsernameCheckResponseDto checkUsernameAvailability(CustomerUsernameCheckRequestDto request) {
        boolean exists = customerRepository.findByUsername(request.getUsername()).isPresent();
        return CustomerUsernameCheckResponseDto.builder()
                .isAvailable(!exists)
                .message(exists ? "이미 존재하는 아이디입니다." : "사용 가능한 아이디입니다.")
                .build();
    }
}
