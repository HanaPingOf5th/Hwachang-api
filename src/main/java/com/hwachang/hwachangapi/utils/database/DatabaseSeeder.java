package com.hwachang.hwachangapi.utils.database;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ApplicationFormRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.CategoryService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
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
    private final ConsultingRoomService consultingRoomService;
    private final ApplicationFormRepository applicationFormRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // user seeding
        CreateTellerRequestDto createTellerRequestDto = CreateTellerRequestDto.builder().tellerNumber("hj1234").name("함형주").position("대리").password("12345").build();
        this.tellerService.signup(createTellerRequestDto);
        CustomerSignupRequestDto customerSignupRequestDto = CustomerSignupRequestDto.builder().username("dw1234").name("김동은").phoneNumber("010-1111-1111").password("12345").build();
        this.customerService.signup(customerSignupRequestDto);

        TellerEntity tellerEntity = this.tellerRepository.findTellerByUserName("hj1234").orElseThrow();
        CustomerEntity customerEntity = this.customerRepository.findByUsername("dw1234").orElseThrow();
        List<UUID> customerIds = new ArrayList<>();
        customerIds.add(customerEntity.getId());
        List<Map<String, Object>> originText = new ArrayList<>();

        Map<String, Object> entry1 = new HashMap<>();
        entry1.put("startTime", "00:00:01");
        entry1.put("endTime", "00:00:05");
        entry1.put("text", "안녕하세요, 대출 상담 관련해서 문의드립니다.");
        entry1.put("speaker", "고객");
        originText.add(entry1);

        Map<String, Object> entry2 = new HashMap<>();
        entry2.put("startTime", "00:00:06");
        entry2.put("endTime", "00:00:10");
        entry2.put("text", "네, 어떤 부분이 궁금하신가요?");
        entry2.put("speaker", "상담원");
        originText.add(entry2);

        // Category
        CategoryEntity categoryEntity1 = CategoryEntity.builder()
                .CategoryName("예금")
                .CategoryType(Type.PERSONAL)
                .build();

        CategoryEntity categoryEntity2 = CategoryEntity.builder()
                .CategoryName("적금")
                .CategoryType(Type.PERSONAL)
                .build();

        categoryRepository.save(categoryEntity1);
        categoryRepository.save(categoryEntity2);

        List<CategoryDto> categoryDtos = categoryService.getCategories();
        UUID categoryId = categoryDtos.get(0).getCategoryId();

        // consultingRooms
        ConsultingRoomEntity consultingRoomEntity = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity.getId())
                .categoryId(categoryId)
                .customerIds(customerIds)
                .originalText(originText)
                .summary("요약내용 입니다.")
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("음성기록url?")
                .time("30min")
                .build();

        this.consultingRoomRepository.save(consultingRoomEntity);

        UUID consultingRoomId = this.consultingRoomRepository.findAll().stream().findFirst().get().getConsultingRoomId();

        // review
        ReviewEntity reviewEntity = ReviewEntity.builder()
                .customerId(customerIds.get(0))
                .tellerId(tellerEntity.getId())
                .nps(10)
                .consultingRoomId(consultingRoomId)
                .build();

        this.reviewRepository.save(reviewEntity);

        // ApplicationForm
        this.applicationFormRepository.createApplicationFormEntity(categoryId);


        CreateTellerRequestDto createTellerRequestDto2 = CreateTellerRequestDto.builder()
                .tellerNumber("jy5678")
                .name("정윤희")
                .position("팀장")
                .password("password5678")
                .build();
        this.tellerService.signup(createTellerRequestDto2);

        CustomerSignupRequestDto customerSignupRequestDto2 = CustomerSignupRequestDto.builder()
                .username("ys7890")
                .name("윤수민")
                .phoneNumber("010-2222-3333")
                .password("password7890")
                .build();
        this.customerService.signup(customerSignupRequestDto2);

        TellerEntity tellerEntity2 = this.tellerRepository.findTellerByUserName("jy5678").orElseThrow();
        CustomerEntity customerEntity2 = this.customerRepository.findByUsername("ys7890").orElseThrow();
        List<UUID> customerIds2 = new ArrayList<>();
        customerIds2.add(customerEntity2.getId());

        List<Map<String, Object>> originText2 = new ArrayList<>();

        Map<String, Object> entry3 = new HashMap<>();
        entry3.put("startTime", "00:00:01");
        entry3.put("endTime", "00:00:07");
        entry3.put("text", "안녕하세요, 적금 상품 추천 부탁드립니다.");
        entry3.put("speaker", "고객");
        originText2.add(entry3);

        Map<String, Object> entry4 = new HashMap<>();
        entry4.put("startTime", "00:00:08");
        entry4.put("endTime", "00:00:15");
        entry4.put("text", "네, 고객님께 적합한 상품을 안내드리겠습니다.");
        entry4.put("speaker", "상담원");
        originText2.add(entry4);

// 새로운 카테고리 추가
        CategoryEntity categoryEntity3 = CategoryEntity.builder()
                .CategoryName("적금 추천")
                .CategoryType(Type.PERSONAL)
                .build();
        categoryRepository.save(categoryEntity3);
        UUID categoryId2 = categoryEntity3.getCategoryId();

// 두 번째 상담방 추가
        ConsultingRoomEntity consultingRoomEntity2 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity2.getId())
                .categoryId(categoryId2)
                .customerIds(customerIds2)
                .originalText(originText2)
                .summary("적금 상담 요약")
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("적금음성기록url")
                .time("25min")
                .build();
        this.consultingRoomRepository.save(consultingRoomEntity2);
        UUID consultingRoomId2 = consultingRoomEntity2.getConsultingRoomId();

// 두 번째 리뷰 추가
        ReviewEntity reviewEntity2 = ReviewEntity.builder()
                .customerId(customerIds2.get(0))
                .tellerId(tellerEntity2.getId())
                .nps(9)
                .consultingRoomId(consultingRoomId2)
                .build();
        this.reviewRepository.save(reviewEntity2);

// 두 번째 ApplicationForm 추가
        this.applicationFormRepository.createSavingsApplicationFormEntity(categoryId2);

    }
}
