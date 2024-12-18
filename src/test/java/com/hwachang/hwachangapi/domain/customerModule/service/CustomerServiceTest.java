package com.hwachang.hwachangapi.domain.customerModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.dto.ConsultingDetailsDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.ConsultingListDto;
import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Status;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.utils.database.AccountRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TellerRepository tellerRepository;

    @Autowired
    private ConsultingRoomRepository consultingRoomRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // **모든 엔티티 초기화**
        consultingRoomRepository.deleteAll();
        tellerRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Transactional
    void testGetCustomerConsultingRecords() {
        // 1. Customer 생성
        CustomerSignupRequestDto customer = CustomerSignupRequestDto.builder()
                .username("customer01")
                .password("pass1234")
                .name("Kim in young")
                .phoneNumber("010-1234-5678")
                .build();
        String customerUsername = customerService.signup(customer);

        CustomerEntity customerEntity = customerRepository.findByUsername(customerUsername)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        UUID customerId = customerEntity.getId();

        // 2. Teller 생성
        TellerEntity teller = TellerEntity.create(
                "teller01",
                "Kim dong eun",
                passwordEncoder.encode("pass1234"),
                AccountRole.Teller,
                "Manager",
                Status.AVAILABLE,
                Type.PERSONAL,
                "https://example.com/profile1.jpg"
        );
        tellerRepository.save(teller);

        // 3. 상담방 생성 및 Customer UUID 추가
        for (int i = 0; i < 3; i++) {
            ConsultingRoomEntity consultingRoom = ConsultingRoomEntity.builder()
                    .tellerId(teller.getId())
                    .categoryId(teller.getId())
                    .customerIds(List.of(customerId)) // 고객 ID 포함
                    .recordChat(List.of("Chat1", "Chat2"))
                    .time("10:00 AM")
                    .title("Test Consulting Room " + i)
                    .voiceRecord("Voice Record")
                    .summary("Summary for Room " + i)
                    .build();
            consultingRoomRepository.save(consultingRoom);
        }

        // 4. 테스트 대상 메서드 호출
        List<ConsultingListDto> consultingRecords = customerService.getCustomerConsultingRecords(customerId);

        // 5. 검증
        assertThat(consultingRecords).hasSize(3);
        consultingRecords.forEach(record -> {
            assertThat(record.getTellerName()).isEqualTo("Kim dong eun");
            assertThat(record.getType()).isEqualTo("개인금융"); // Type.PERSONAL의 설명
            assertThat(record.getCategory()).isEqualTo("PERSONAL");
            assertThat(record.getSummary()).contains("Summary for Room");
            assertThat(record.getDate()).isNotNull();
        });

        // 6. 상세보기 테스트 - 첫 번째 상담 기록 상세보기
        ConsultingListDto firstRecord = consultingRecords.get(0);
        UUID consultingRoomId = firstRecord.getConsultingRoomId();

        ConsultingDetailsDto consultingDetails = customerService.getConsultingDetails(customerId, consultingRoomId);

        // 7. 검증 - 상세보기
        assertThat(consultingDetails.getSummary()).isEqualTo("Summary for Room 0");
        assertThat(consultingDetails.getTellerName()).isEqualTo("Kim dong eun");
        assertThat(consultingDetails.getType()).isEqualTo("PERSONAL");
        assertThat(consultingDetails.getCategory()).isEqualTo("개인금융");
        assertThat(consultingDetails.getDate()).isNotNull();
        assertThat(consultingDetails.getOriginalText()).isNull(); // 예제에서는 OriginalText 미설정

        // 추가: 모든 상담 기록에 대해 상세보기 테스트 반복 가능
        consultingRecords.forEach(record -> {
            ConsultingDetailsDto details = customerService.getConsultingDetails(customerId, record.getConsultingRoomId());
            assertThat(details.getSummary()).contains("Summary for Room");
            assertThat(details.getTellerName()).isEqualTo("Kim dong eun");
        });
    }
}