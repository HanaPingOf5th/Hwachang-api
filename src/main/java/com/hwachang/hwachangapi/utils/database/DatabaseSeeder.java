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

        saveCategory("예금", true);saveCategory("신탁/ISA", true);
        saveCategory("펀드", true);saveCategory("대출", true);
        saveCategory("퇴직연금", true);saveCategory("전자금융", true);
        saveCategory("주택청약", true);saveCategory("파생상품", true);
        saveCategory("외환", true);saveCategory("자동이체", true);
        saveCategory("보험", true);saveCategory("기타", true);

        saveCategory("기업", false);saveCategory("예금", false);
        saveCategory("대출", false);saveCategory("퇴직연금", false);
        saveCategory("펀드", false);saveCategory("외환", false);
        saveCategory("전자금융", false);saveCategory("파생상품", false);
        saveCategory("오픈 뱅킹", false);saveCategory("우수고객", false);
        saveCategory("예금", false);saveCategory("기타", false);



        List<CategoryDto> categories = categoryService.getCategories();
        categories.forEach(category -> createFormsForCategory(category.getCategoryId(), category.getCategoryName()));
        categories.forEach(category -> createDocumentsForCategory(category.getCategoryId(), category.getCategoryName(), category.getCategoryType()));
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
                .title("제목1")
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
                .title("제목2")
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
                .title("제목3")
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

        ConsultingRoomEntity consultingRoom5 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity2.getId())
                .categoryId(savingsCategoryId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(null)
                .summary(null)
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("https://kr.object.ncloudstorage.com/consulting-audiofile/consulting-data-b2d6d2c8-135f-4b23-b912-14bf39a312b5.mp4")
                .time("2024-12-28")
                .build();

        consultingRoomRepository.save(consultingRoom1);
        consultingRoomRepository.save(consultingRoom2);
        consultingRoomRepository.save(consultingRoom3);
        consultingRoomRepository.save(consultingRoom4);
        consultingRoomRepository.save(consultingRoom5);

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

    private void createDocumentsForCategory(UUID categoryId, String categoryName, Type type) {
        if(type.equals(Type.PERSONAL)) {
            if (categoryName.equals("이체")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("보이스피싱 예방 가이드")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070101/__icsFiles/afieldfile/2024/12/18/GuidesForPreventingVoicePhishing-20241216.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("이체 서비스 이용약관")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070101/__icsFiles/afieldfile/2024/08/08/3-11-0012.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("예금(신탁) 양도승낙 의뢰서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070101/__icsFiles/afieldfile/2020/05/08/5-08-0037_200508.pdf")
                        .build());
            } else if (categoryName.equals("대출")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("대출 상품설명서(권유용)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070102/__icsFiles/afieldfile/2023/12/14/3060148_231214.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("대출정보 열람청구 및 상환 위임장")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070102/__icsFiles/afieldfile/2023/05/31/5-16-0365_230531.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("[필수] 개인(신용)정보 수집 · 이용 · 제공 동의서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070102/__icsFiles/afieldfile/2021/03/02/5-06-0813_20210302.pdf")
                        .build());
            } else if (categoryName.equals("신탁/ISA")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("[필수] 개인(신용)정보 수집 · 이용 · 제공 동의서(은행보관용)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070107/__icsFiles/afieldfile/2021/05/27/3-21-0004_20210510.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("위임장 (일임형 개인종합자산관리계좌(ISA)용)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070107/__icsFiles/afieldfile/2021/08/26/5210001_20210115.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("계약대상자 확인서 (은행보관용)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070107/__icsFiles/afieldfile/2024/08/20/3-14-0034_20240821.pdf")
                        .build());
            } else if (categoryName.equals("퇴직연금")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("퇴직연금 현재운용상품변경 신청서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070108/__icsFiles/afieldfile/2024/10/15/5-14-0046.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("퇴직연금 거래신청서(개인형IRP)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070108/__icsFiles/afieldfile/2024/10/02/5-14-0020.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("퇴직연금 계약해지 신청서(개인형IRP)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070108/__icsFiles/afieldfile/2024/07/18/5-14-0044.pdf")
                        .build());
            } else if (categoryName.equals("펀드")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("공모부동산집합투자증권 과세특례 신청서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070109/__icsFiles/afieldfile/2021/01/15/5190014_20210104.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("위임장 (집합투자증권 및 연금저축계좌 투자용)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070109/__icsFiles/afieldfile/2021/08/19/3-14-0231_20210819.pdf")
                        .build());
            } else if (categoryName.equals("외환")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("해 외 직 접 투 자 신 고 서(보고서)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070103/__icsFiles/afieldfile/2024/04/25/5-09-0290.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("외화송금의 내용변경 및 취소, 송금수표 분실신고 등 신청서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070103/__icsFiles/afieldfile/2022/12/21/5-09-0064.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("외국인투자기업등록신청서 [ ]신규등록 [ ]변경등록")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070103/__icsFiles/afieldfile/2022/11/11/5_09_0206_8.pdf")
                        .build());
            } else if (categoryName.equals("전자금융")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("금융결제원CMS 이용계약서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070104/__icsFiles/afieldfile/2021/07/01/b000020130313_20090921.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("[필수] 개인(신용)정보 수집·이용 동의서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070104/__icsFiles/afieldfile/2021/02/04/2021_20210204_01.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("Hana 1Q bank CMSiNet 서비스 이용 추가 계약서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070104/__icsFiles/afieldfile/2021/01/08/5-08-0502_210108.pdf")
                        .build());
            } else if (categoryName.equals("파생상품")) {
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("고령(만65세~79세) 투자자확인서(장외파생상품용)")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070105/__icsFiles/afieldfile/2022/04/06/5-09-0465_1.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("장외파생상품 일반투자자 투자자정보 분석결과표 ")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070105/__icsFiles/afieldfile/2023/08/18/5-09-0526.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("장외파생상품 일반투자자 투자자정보 확인서")
                        .path("https://image.kebhana.com/cont/customer/customer07/customer0701/customer070105/__icsFiles/afieldfile/2023/08/18/5-09-0095.pdf")
                        .build());
            }
        }else{
            if(categoryName.equals("기업")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("기업 전자금융서비스 신청서(은행용)")
                        .path("https://biz.kebhana.com/cont/pdf/terms/3080024_f.pdf")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("기업 전자금융서비스 신청서(은행용)")
                        .path("https://biz.kebhana.com/cont/pdf/terms/3080024_f.pdf")
                        .build());
            } else if(categoryName.equals("예금")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("예금(신탁) 잔액증명 의뢰서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070101/1418359_115299.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("자동이체 신청서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070101/1502795_115299.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("금융거래의 목적 확인 안내")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070101/1503182_115299.jsp")
                        .build());
            } else if(categoryName.equals("대출")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("융자상담 및 차입신청서(기업용)")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070102/1418365_115298.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("적합성•적정성 정보파악용 고객정보 확인서(법인)")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070102/1501962_115298.jsp")
                        .build());
            } else if(categoryName.equals("퇴직연금")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("퇴직연금 거래신청서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070108/1452339_137475.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("퇴직급여 지급신청서(DC, 기업형IRP)")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070108/1452345_137475.jsp")
                        .build());
            } else if(categoryName.equals("외환")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("해 외 직 접 투 자 신 고 서(보고서)")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070103/1432123_115297.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("해 외 직 접 투 자 신 고 서(보고서)")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070103/1432125_115297.jsp")
                        .build());
            } else if(categoryName.equals("전자금융")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("CMS Global 이용 신청서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070104/1418303_115296.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("하나 sERP 상담 신청서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070104/1501965_115296.jsp")
                        .build());
            } else if(categoryName.equals("파생 상품")){
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("장외파생상품 일반투자자 투자자정보 확인서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070105/1433947_115294.jsp")
                        .build());
                documentRepository.save(DocumentEntity.builder()
                        .categoryId(categoryId)
                        .title("HANA FX TRADING SYSTEM 이용신청서")
                        .path("https://www.hanabank.com/cont/customer/customer07/customer0701/customer070105/1468448_115294.jsp")
                        .build());
            }
        }
    }

}
