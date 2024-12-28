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
    private final ApplicationFormRepository applicationFormRepository;
    private final DocumentRepository documentRepository;
    private final ReviewRepository reviewRepository;

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

        CreateTellerRequestDto tellerRequestDto2 = CreateTellerRequestDto.builder()
                .tellerNumber("sz1234")
                .name("임수진")
                .position("대리")
                .password("12345")
                .build();
        this.tellerService.signupP(tellerRequestDto2);

        CustomerSignupRequestDto customerRequestDto = CustomerSignupRequestDto.builder()
                .username("dw1234")
                .name("김동은")
                .phoneNumber("010-1111-1111")
                .password("12345")
                .build();
        this.customerService.signup(customerRequestDto);

        TellerEntity tellerEntity1 = this.tellerRepository.findTellerByUserName("hj1234").orElseThrow();
        TellerEntity tellerEntity2 = this.tellerRepository.findTellerByUserName("sz1234").orElseThrow();

        CustomerEntity customerEntity = this.customerRepository.findByUsername("dw1234").orElseThrow();

        // Creating categories
        CategoryEntity depositCategory = CategoryEntity.builder()
                .categoryName("예금")
                .categoryType(Type.PERSONAL)
                .build();
        CategoryEntity savingsCategory = CategoryEntity.builder()
                .categoryName("적금")
                .categoryType(Type.PERSONAL)
                .build();
        categoryRepository.save(depositCategory);
        categoryRepository.save(savingsCategory);

        saveCategory("예금", true);saveCategory("펀드/신택", true);
        saveCategory("카드", true);saveCategory("대출", true);
        saveCategory("스마트뱅킹", true);saveCategory("인증서", true);
        saveCategory("주택청약", true);saveCategory("텔레뱅킹", true);
        saveCategory("금융사기", true);saveCategory("자동이체", true);
        saveCategory("보험", true);saveCategory("기타", true);

        saveCategory("대출", false);saveCategory("자동이체", false);
        saveCategory("외환", false);saveCategory("금융사기", false);
        saveCategory("입출금 알림", false);saveCategory("텔레뱅킹", false);
        saveCategory("펀드/신탁", false);saveCategory("인증서", false);
        saveCategory("오픈 뱅킹", false);saveCategory("우수고객", false);
        saveCategory("예금", false);saveCategory("기타", false);



        List<CategoryDto> categories = categoryService.getCategories();
        categories.forEach(category -> createFormsForCategory(category.getCategoryId(), category.getCategoryName()));
        categories.forEach(category -> createDocumentsForCategory(category.getCategoryId(), category.getCategoryName()));
        UUID depositCategoryId = categories.get(0).getCategoryId();
        UUID savingsCategoryId = categories.get(1).getCategoryId();



        // Create two consulting rooms for the same customer and teller
        List<Map<String, Object>> consultingRoom1Text = new ArrayList<>();
        consultingRoom1Text.add(createTextEntry("00:00:01", "00:00:05", "안녕하세요, 대출 상담 관련해서 문의드립니다.", "고객"));
        consultingRoom1Text.add(createTextEntry("00:00:06", "00:00:10", "네, 어떤 부분이 궁금하신가요?", "상담원"));

        ConsultingRoomEntity consultingRoom1 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity1.getId())
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
                .time("2024-10-10")
                .build();

        List<Map<String, Object>> consultingRoom2Text = new ArrayList<>();
        consultingRoom2Text.add(createTextEntry("00:00:01", "00:00:07", "안녕하세요, 적금 상품 추천 부탁드립니다.", "고객"));
        consultingRoom2Text.add(createTextEntry("00:00:08", "00:00:15", "네, 적합한 상품을 안내드리겠습니다.", "상담원"));

        ConsultingRoomEntity consultingRoom2 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity1.getId())
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
                .time("2024-10-11")
                .build();

        List<Map<String, Object>> consultingRoom3Text = new ArrayList<>();
        consultingRoom3Text.add(createTextEntry("00:00:01", "00:00:09", "안녕하세요. 요즘 금리가 높 은 예금 상품이 있다고 들었 는데요. 어떤 상품이 있는지 알려주실 수 있나요?", "발화자1"));
        consultingRoom3Text.add(createTextEntry("00:00:10", "00:00:17", "안녕하세요. 저희 은행에서는 지금 금리가 높은 정기예금 상품으로 스마트 정기 예금 이 있습니다. 기본 금리가 4.5%이고 1년 만기로 운 영됩니다.", "발화자1"));
        consultingRoom3Text.add(createTextEntry("00:00:18", "00:00:25", "조건이 따로 있나요? 가입 최소 금액은 100만 원이고 모바일 앱으로 가입하시면 추가 금액의 0.2% 수익을 받으실 수 있습니다.", "발화자1"));
        consultingRoom3Text.add(createTextEntry("00:00:26", "00:00:33", "모바일 앱 가입이라면 은행에 직접 방문하지 않아도 되나 요? 네 맞습니다. 앱에서 간단히 본인 인증 후 바로 가입하실 수 있습니다.", "발화자1"));
        consultingRoom3Text.add(createTextEntry("00:00:34", "00:00:42", "필요하시면 가입 방법도 안내 해 드릴게요. 좋아요. 안내 부탁드려요.", "발화자1"));

        ConsultingRoomEntity consultingRoom3 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity1.getId())
                .categoryId(savingsCategoryId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom3Text)
                .summary("주요주제 : 금리 높은 예금 상품 문의" +
                        " - 금리 높은 예금 상품 문의함" +
                        " - 해당 은행에는 스마트 정기 예금이 있음 (기본금리 4.5%, 1년 만기)" +
                        " - 가입 최소 금액은 100만원, 모바일 앱으로 가입시 추가금액의 0.2% 수익 가능" +
                        " - 모바일 앱 가입이므로 은행 방문 불필요하며 간단한 본인 인증 후 가입 가능"
                )
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("https://kr.object.ncloudstorage.com/consulting-audiofile/consulting-data-d9a2f5f4-768a-4ebf-8b6b-445f2250c194.mp4")
                .time("2024-12-28")
                .build();

        List<Map<String, Object>> consultingRoom4Text = new ArrayList<>();
        consultingRoom4Text.add(createTextEntry("00:00:01", "00:00:09", "도는 하고 지금 적금을 알아 보고 있습니다. 추천해 주실 만한 상품이 있을까요?", "발화자1"));
        consultingRoom4Text.add(createTextEntry("00:00:10", "00:00:17", "안녕하세요. 은행의 드림 적 금 상품을 추천드립니다. 월 납입 금액과 기간에 따라 다양한 옵션이 있습니다.", "발화자1"));
        consultingRoom4Text.add(createTextEntry("00:00:18", "00:00:25", "이 상품이 어떻게 되나요? 기본 금리는 3.5%이고 1 년 이상 유지하면 최대 4% 까지 받을 수 있습니다.", "발화자1"));
        consultingRoom4Text.add(createTextEntry("00:00:26", "00:00:33", "감사합니다.", "발화자1"));

        ConsultingRoomEntity consultingRoom4 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity2.getId())
                .categoryId(savingsCategoryId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom4Text)
                .summary("주요주제 : 금리 높은 예금 상품 문의" +
                        " - 금리 높은 정기예금 상품 문의" +
                        " - 해당 상품 설명, 기본금리 4.5%, 1년 만기" +
                        " - 가입조건 확인, 최소금액 100만원, 모바일앱 가입 시 추가금리 0.2%" +
                        " - 모바일앱 가입시 은행 방문 불필요함 알림" +
                        " - 가입방법 안내"
                )
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("https://kr.object.ncloudstorage.com/consulting-audiofile/consulting-data-b2d6d2c8-135f-4b23-b912-14bf39a312b5.mp4")
                .time("2024-12-28")
                .build();

        consultingRoomRepository.save(consultingRoom1);
        consultingRoomRepository.save(consultingRoom2);
        consultingRoomRepository.save(consultingRoom3);
        consultingRoomRepository.save(consultingRoom4);


        //create review
        ReviewEntity reviewEntity1 = ReviewEntity.builder()
                .customerId(customerEntity.getId())
                .tellerId(tellerEntity1.getId())
                .nps(10)
                .content("훌륭함")
                .consultingRoomId(consultingRoom1.getConsultingRoomId())
                .build();
        ReviewEntity reviewEntity2 = ReviewEntity.builder()
                .customerId(customerEntity.getId())
                .tellerId(tellerEntity1.getId())
                .nps(6)
                .content("부족함")
                .consultingRoomId(consultingRoom1.getConsultingRoomId())
                .build();
        reviewRepository.save(reviewEntity1);
        reviewRepository.save(reviewEntity2);
    }

    private void saveCategory(String name, Boolean isPersonal) {
        CategoryEntity category = CategoryEntity.builder()
                .categoryName(name)
                .categoryType(isPersonal?Type.PERSONAL:Type.CORPORATE)
                .build();
        categoryRepository.save(category);
    }

    private Map<String, Object> createTextEntry(String startTime, String endTime, String text, String speaker) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("startTime", startTime);
        entry.put("endTime", endTime);
        entry.put("text", text);
        entry.put("speaker", speaker);
        return entry;
    }

    private void createFormsForCategory(UUID categoryId, String categoryName) {
        for (int i = 1; i <= 3; i++) {
            UUID formId = UUID.randomUUID();
            String title = categoryName + " 신청서 " + i;
            String applicationForm = "{ \"title\": \"" + title + "\", " +
                    "   \"customerInfo\": { \"name\": \"김인영\", \"residentNumber\": \"123456-7890123\", \"address\": \"서울특별시\" }, " +
                    "   \"subjects\": [ " +
                    "     { \"title\": \"상담 항목\", " +
                    "       \"items\": [ " +
                    "         { \"type\": \"input\", \"description\": \"상품명\" }, " +
                    "         { \"type\": \"input\", \"description\": \"계약기간\" }, " +
                    "         { \"type\": \"input\", \"description\": \"금액\" } " +
                    "       ] " +
                    "     } " +
                    "   ] " +
                    " }";

            applicationFormRepository.createApplicationFormEntity(formId, categoryId, title, applicationForm);
        }
    }

    private void createDocumentsForCategory(UUID categoryId, String categoryName) {
        for (int i = 1; i <= 3; i++) {
            documentRepository.save(DocumentEntity.builder()
                    .categoryId(categoryId)
                    .title(categoryName + " 관련 서류 " + i)
                    .path("https://example.com/" + categoryName + "/document" + i)
                    .build());
        }
    }
}
