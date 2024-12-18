package com.hwachang.hwachangapi.utils.database;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.DocumentEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ApplicationFormRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.DocumentRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.CategoryService;
import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.service.CustomerService;
import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final TellerService tellerService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final TellerRepository tellerRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final ConsultingRoomRepository consultingRoomRepository;
    private final ApplicationFormRepository applicationFormRepository;
    private final DocumentRepository documentRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Seeding common user and teller
        CreateTellerRequestDto tellerRequestDto = CreateTellerRequestDto.builder()
                .tellerNumber("hj1234")
                .name("함형주")
                .position("대리")
                .password("12345")
                .build();
        this.tellerService.signup(tellerRequestDto);

        CustomerSignupRequestDto customerRequestDto = CustomerSignupRequestDto.builder()
                .username("dw1234")
                .name("김동은")
                .phoneNumber("010-1111-1111")
                .password("12345")
                .build();
        this.customerService.signup(customerRequestDto);

        TellerEntity tellerEntity = this.tellerRepository.findTellerByUserName("hj1234").orElseThrow();
        CustomerEntity customerEntity = this.customerRepository.findByUsername("dw1234").orElseThrow();

        // Creating categories
        CategoryEntity depositCategory = CategoryEntity.builder()
                .CategoryName("예금")
                .CategoryType(Type.PERSONAL)
                .build();
        CategoryEntity savingsCategory = CategoryEntity.builder()
                .CategoryName("적금")
                .CategoryType(Type.PERSONAL)
                .build();
        categoryRepository.save(depositCategory);
        categoryRepository.save(savingsCategory);

        List<CategoryDto> categories = categoryService.getCategories();
        UUID depositCategoryId = categories.get(0).getCategoryId();
        UUID savingsCategoryId = categories.get(1).getCategoryId();

        // Creating two consulting rooms for the same customer and teller
        List<Map<String, Object>> consultingRoom1Text = new ArrayList<>();
        consultingRoom1Text.add(createTextEntry("00:00:01", "00:00:05", "안녕하세요, 대출 상담 관련해서 문의드립니다.", "고객"));
        consultingRoom1Text.add(createTextEntry("00:00:06", "00:00:10", "네, 어떤 부분이 궁금하신가요?", "상담원"));

        ConsultingRoomEntity consultingRoom1 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity.getId())
                .categoryId(depositCategoryId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom1Text)
                .summary("주요주제 : 개인상담과 기업상담은 주제가 다름" +
                        " - 개인상담은 개인위주로 해주는 상담임" +
                        " - 기업상담은 기업위주로 해주는 상담임" +
                        " - 둘은 합칠 수 없음"
                )
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("voice1.url")
                .time("20min")
                .build();

        List<Map<String, Object>> consultingRoom2Text = new ArrayList<>();
        consultingRoom2Text.add(createTextEntry("00:00:01", "00:00:07", "안녕하세요, 적금 상품 추천 부탁드립니다.", "고객"));
        consultingRoom2Text.add(createTextEntry("00:00:08", "00:00:15", "네, 적합한 상품을 안내드리겠습니다.", "상담원"));

        ConsultingRoomEntity consultingRoom2 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity.getId())
                .categoryId(savingsCategoryId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom2Text)
                .summary("주요주제 : 예금과 적금은 하는 방법과 효율이 다름" +
                        " - 예금은 이율이 좋지 않음" +
                        " - 적금은 이율이 좋음" +
                        " - 예금과 적금은 다름"
                )
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("voice2.url")
                .time("30min")
                .build();

        consultingRoomRepository.save(consultingRoom1);
        consultingRoomRepository.save(consultingRoom2);

        // Creating application forms for categories
        applicationFormRepository.createApplicationFormEntity(depositCategoryId);
        applicationFormRepository.createSavingsApplicationFormEntity(savingsCategoryId);

        // create document

        documentRepository.save(DocumentEntity.builder()
                .categoryId(categories.get(0).getCategoryId())
                .title("예금관련 서류")
                .path("https://www.naver.com/")
                .build());
        documentRepository.save(DocumentEntity.builder()
                .categoryId(categories.get(0).getCategoryId())
                .title("예금관련 서류2")
                .path("https://www.naver.com/")
                .build());
        documentRepository.save(DocumentEntity.builder()
                .categoryId(categories.get(0).getCategoryId())
                .title("예금관련 서류3")
                .path("https://www.naver.com/")
                .build());
        documentRepository.save(DocumentEntity.builder()
                .categoryId(categories.get(1).getCategoryId())
                .title("적금관련 서류1")
                .path("https://www.naver.com/")
                .build());
        documentRepository.save(DocumentEntity.builder()
                .categoryId(categories.get(1).getCategoryId())
                .title("적금관련 서류2")
                .path("https://www.naver.com/")
                .build());
    }

    private Map<String, Object> createTextEntry(String startTime, String endTime, String text, String speaker) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("startTime", startTime);
        entry.put("endTime", endTime);
        entry.put("text", text);
        entry.put("speaker", speaker);
        return entry;
    }
}
