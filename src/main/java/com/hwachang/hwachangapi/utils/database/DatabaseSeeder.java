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
        CategoryEntity overseasRemittance = CategoryEntity.builder()
                .categoryName("해외송금")
                .categoryType(Type.PERSONAL)
                .build();
        CategoryEntity retirementPension = CategoryEntity.builder()
                .categoryName("퇴직연금")
                .categoryType(Type.PERSONAL)
                .build();
        CategoryEntity fund = CategoryEntity.builder()
                .categoryName("펀드")
                .categoryType(Type.CORPORATE)
                .build();
        categoryRepository.save(depositCategory);
        categoryRepository.save(savingsCategory);
        categoryRepository.save(overseasRemittance);
        categoryRepository.save(retirementPension);
        categoryRepository.save(fund);


        saveCategory("신탁/ISA", true);
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
        UUID overseasRemittanceId = categories.get(2).getCategoryId();
        UUID retirementPensionId = categories.get(3).getCategoryId();
        UUID fundId = categories.get(4).getCategoryId();



        // Create two consulting rooms for the same customer and teller
        List<Map<String, Object>> consultingRoom1Text = new ArrayList<>();
        consultingRoom1Text.add(createTextEntry("00:00:01", "00:00:05", "안녕하세요, 대출 상담 관련해서 문의드립니다.", "고객"));
        consultingRoom1Text.add(createTextEntry("00:00:06", "00:00:10", "네, 어떤 부분이 궁금하신가요?", "상담원"));

        ConsultingRoomEntity consultingRoom1 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity1.getId())
                .categoryId(depositCategoryId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom1Text)
                .title("개인상담과 기업상담의 차이")
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
                .title("예금과 적금의 차이")
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
                .title("금리 높은 예금 상품 문의")
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
                .title("금리 높은 적금 상품 문의")
                .summary("주요주제 : 금리 높은 적금 상품 문의" +
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

        List<Map<String, Object>> consultingRoom5Text = new ArrayList<>();
        consultingRoom5Text.add(createTextEntry("00:00:05", "00:00:12", "안녕하세요. 하나은행 외환 상담 센터입니다. 오늘은 어떤 도움을 드릴까요?", "발화자1"));
        consultingRoom5Text.add(createTextEntry("00:00:13", "00:00:22", "안녕하세요. 해외 송금에 대해 문의드리고 싶어서요. 송금 절차와 수수료에 대해 알고 싶습니다.", "발화자2"));
        consultingRoom5Text.add(createTextEntry("00:00:23", "00:00:33", "네, 해외 송금은 수취 은행 정보와 송금 금액만 입력하시면 진행 가능합니다. 기본 수수료는 5천 원부터 시작합니다.", "발화자1"));
        consultingRoom5Text.add(createTextEntry("00:00:34", "00:00:41", "그렇군요. 환율은 실시간으로 적용되나요? 혹시 예약 송금도 가능한가요?", "발화자2"));
        consultingRoom5Text.add(createTextEntry("00:00:42", "00:00:49", "네, 환율은 실시간으로 적용되며, 예약 송금 서비스도 제공됩니다. 예약 시점의 환율로 송금 가능합니다.", "발화자1"));
        consultingRoom5Text.add(createTextEntry("00:00:50", "00:01:04", "송금이 완료되기까지 보통 얼마나 걸리나요? 그리고 해외 계좌에서 바로 입금 확인이 가능한가요?", "발화자2"));
        consultingRoom5Text.add(createTextEntry("00:01:05", "00:01:13", "송금 소요 시간은 대략 1~2 영업일입니다. 다만 국가와 은행에 따라 차이가 있을 수 있습니다. 입금 확인은 송금 완료 후 수취인 은행에서 확인 가능합니다.", "발화자1"));
        consultingRoom5Text.add(createTextEntry("00:01:14", "00:01:27", "알겠습니다. 그리고 환율 우대 혜택 같은 것도 있나요?", "발화자2"));
        consultingRoom5Text.add(createTextEntry("00:01:28", "00:01:36", "네, 하나은행은 외환 거래 고객에게 최대 50% 환율 우대 혜택을 제공합니다. 자세한 내용은 홈페이지나 모바일 앱에서 확인하실 수 있습니다.", "발화자1"));
        consultingRoom5Text.add(createTextEntry("00:01:37", "00:01:42", "좋은 정보 감사합니다. 자세히 알아보겠습니다.", "발화자2"));
        consultingRoom5Text.add(createTextEntry("00:01:43", "00:01:47", "천만에요. 더 궁금하신 점이 있으면 언제든 문의해주세요!", "발화자1"));



        ConsultingRoomEntity consultingRoom5 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity2.getId())
                .categoryId(overseasRemittanceId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom5Text)
                .title("해외송금 방법, 수수료, 환율 등 문의")
                .summary("주요주제 : 해외송금 방법, 수수료, 환율 등 문의" +
                        " - 해외 송금은 수취 은행 정보와 송금 금액만 입력하면 진행 가능함" +
                        " - 기본 수수료는 5천원 부터 시작하며, 환율은 실시간으로 제공됨" +
                        " - 예약 송금 서비스 이용 시 예약 시점의 환율로 송금 가능 함" +
                        " - 송금 소요 시간은 대략 1~2영업일이며, 입금 확인은 송금 완료 후 수취인 은행에서 확인 가능" +
                        " - 하나은행은 외환 거래 고객에게 최대 50% 환율 우대 혜택 제공")
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("https://kr.object.ncloudstorage.com/consulting-audiofile/consulting-data-981971d9-bdf4-40b6-8d99-611d0bfc4bf4.mp4")
                .time("2024-11-11")
                .build();

        List<Map<String, Object>> consultingRoom6Text = new ArrayList<>();
        consultingRoom6Text.add(createTextEntry("00:00:05", "00:00:12", "안녕하세요. 하나은행을 찾아 주셔서 감사합니다. 오늘은 어떤 상담을 도와드릴까요?", "발화자1"));
        consultingRoom6Text.add(createTextEntry("00:00:13", "00:00:22", "안녕하세요. 은행의 드림 적 금 상품을 추천드립니다. 월 납입 금액과 기간에 따라 다양한 옵션이 있습니다.", "발화자2"));
        consultingRoom6Text.add(createTextEntry("00:00:23", "00:00:33", "이 상품이 어떻게 되나요? 기본 금리는 3.5%이고 1 년 이상 유지하면 최대 4% 까지 받을 수 있습니다.", "발화자1"));
        consultingRoom6Text.add(createTextEntry("00:00:34", "00:00:41", "아직 잘 모르겠어요. 두 가 지 차이를 설명해 주실 수 있을까요?.", "발화자2"));
        consultingRoom6Text.add(createTextEntry("00:00:42", "00:00:49", "물론이죠. DB형은 회사가 퇴직 후 받을 금액을 보장하 는 방식으로 회사가 운영 책 임을 집니다.", "발화자1"));
        consultingRoom6Text.add(createTextEntry("00:00:50", "00:01:04", "DCR은 매년 회사가 일정 금액으로 고객님 계좌에 납입 하고 고객님이 직접 투자 방 향을 결정하는 방식입니다. 혹시 투자에 관심이 있으신가 요? 아니면 안정적인 방식이 더 좋으신가요?", "발화자2"));
        consultingRoom6Text.add(createTextEntry("00:01:05", "00:01:13", "저는 투자 경험은 없어서 안 정적인 방식이 더 좋을 것 같습니다. 그러면 DC형이 더 수익이 높은가요?", "발화자1"));
        consultingRoom6Text.add(createTextEntry("00:01:14", "00:01:27", "네 DCR는 본인 투자 선택 에 따라 수익을 극대화할 수 있는 가능성이 있지만 그만 큼 위험도 있을 수 있습니다 . 반대로 DB형은 안정적이 지만 수익률 면에서는 상대적 으로 낮을 수 있죠.", "발화자2"));
        consultingRoom6Text.add(createTextEntry("00:01:28", "00:01:32", "감사합니다.", "발화자1"));

        ConsultingRoomEntity consultingRoom6 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity2.getId())
                .categoryId(retirementPensionId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoom6Text)
                .title("퇴직연금제도 (DB형, DC형) 안내")
                .summary("주요주제 : 퇴직연금 제도 (DB형, DC형) 안내" +
                        " - 퇴직연금에는 DB형과 DC형이 있음" +
                        " - DB형은 회사가 퇴직 후 받을 금액을 보장하며 회사가 운영함" +
                        " - DC형은 회사가 매년 일정 금액을 납입하면 고객이 직접 투자방향을 결정함" +
                        " - 투자경험이 없는 경우 안정적인 DB형이 적합하나 수익률은 상대적으로 낮음")
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("https://kr.object.ncloudstorage.com/consulting-audiofile/consulting-data-c9b933e0-d831-4a10-aea0-c4293ccb53ec.mp4")
                .time("2024-10-10")
                .build();

        List<Map<String, Object>> consultingRoomFundText = new ArrayList<>();
        consultingRoomFundText.add(createTextEntry("00:00:05", "00:00:12", "안녕하세요. 하나은행 기업금융 상담 센터입니다. 어떤 도움을 드릴까요?", "발화자1"));
        consultingRoomFundText.add(createTextEntry("00:00:13", "00:00:22", "안녕하세요. 회사 운영 자금을 위해 펀드 상품을 알아보고 있습니다. 어떤 상품이 있는지 궁금합니다.", "발화자2"));
        consultingRoomFundText.add(createTextEntry("00:00:23", "00:00:33", "네, 기업을 위한 펀드 상품은 안정적인 채권형 펀드부터 수익성이 높은 주식형 펀드까지 다양합니다. 회사의 투자 목적에 따라 추천드릴 수 있습니다.", "발화자1"));
        consultingRoomFundText.add(createTextEntry("00:00:34", "00:00:41", "저희는 안정성과 수익성을 모두 고려하고 싶습니다. 추천해 주실 만한 상품이 있을까요?", "발화자2"));
        consultingRoomFundText.add(createTextEntry("00:00:42", "00:00:49", "네, 혼합형 펀드가 적합할 것 같습니다. 안정적인 채권과 수익성 있는 주식에 분산 투자하여 위험을 줄이면서도 수익을 기대할 수 있습니다.", "발화자1"));
        consultingRoomFundText.add(createTextEntry("00:00:50", "00:01:04", "좋네요. 그런데 펀드 운용 방식과 수수료 구조는 어떻게 되나요?", "발화자2"));
        consultingRoomFundText.add(createTextEntry("00:01:05", "00:01:13", "펀드는 전문가가 운용하며, 고객님께서는 정기적으로 운용 성과를 확인할 수 있습니다. 수수료는 가입 시점과 운용 단계에서 각각 부과되며, 상품에 따라 차이가 있습니다.", "발화자1"));
        consultingRoomFundText.add(createTextEntry("00:01:14", "00:01:27", "그렇군요. 혹시 환매 시점은 어떻게 결정되나요? 긴급 자금이 필요할 때 바로 찾을 수 있을까요?", "발화자2"));
        consultingRoomFundText.add(createTextEntry("00:01:28", "00:01:36", "환매는 상품 유형에 따라 다르지만, 일반적으로 펀드 환매는 1~3 영업일 내에 처리됩니다. 긴급 상황에서는 유동성 높은 상품을 추천드립니다.", "발화자1"));
        consultingRoomFundText.add(createTextEntry("00:01:37", "00:01:42", "알겠습니다. 감사합니다.", "발화자2"));

        ConsultingRoomEntity consultingRoom7 = ConsultingRoomEntity.builder()
                .tellerId(tellerEntity1.getId())
                .categoryId(fundId)
                .customerIds(Collections.singletonList(customerEntity.getId()))
                .originalText(consultingRoomFundText)
                .title("기업용 펀드 상품 문의")
                .summary("주요주제 : 기업용 펀드 상품 문의" +
                        " - 회사 운영 자금 목적으로 펀드 상품 탐색중" +
                        " - 안정성과 수익성 둘 다 고려 희망" +
                        " - 산업형 펀드 추천 받음" +
                        " - 펀드 운용 방식, 수수료 구조, 판매 시점 등 추가 정보 요구" +
                        " - 긴급상황시 유동성 높은 상품 추천받음")
                .recordChat(new ArrayList<>())
                .voiceRecordUrl("https://kr.object.ncloudstorage.com/consulting-audiofile/consulting-data-7d4bdf08-28a7-4955-adf0-bcd81bb78dfa.mp4")
                .time("2024-10-10")
                .build();

        consultingRoomRepository.save(consultingRoom1);
        consultingRoomRepository.save(consultingRoom2);
        consultingRoomRepository.save(consultingRoom3);
        consultingRoomRepository.save(consultingRoom4);
        consultingRoomRepository.save(consultingRoom5);
        consultingRoomRepository.save(consultingRoom6);
        consultingRoomRepository.save(consultingRoom7);


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
