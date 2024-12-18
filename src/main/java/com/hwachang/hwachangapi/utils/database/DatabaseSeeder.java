package com.hwachang.hwachangapi.utils.database;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.service.CustomerService;
import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final TellerService tellerService;
    private final CustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
        // user seeding
        CreateTellerRequestDto createTellerRequestDto = CreateTellerRequestDto.builder().tellerNumber("hj1234").name("함형주").position("대리").password("12345").build();
        this.tellerService.signup(createTellerRequestDto);
        CustomerSignupRequestDto customerSignupRequestDto = CustomerSignupRequestDto.builder().username("dw1234").name("김동은").phoneNumber("010-1111-1111").password("12345").build();
        this.customerService.signup(customerSignupRequestDto);

        // Category
//        CategoryEntity categoryEntity = CategoryEntity.builder().CategoryName("기업").

        // consultingRoom
//        ConsultingRoomEntity consultingRoomEntity = ConsultingRoomEntity.builder()
//                .title("상담방1")
//                .time("30min")
//                .categoryId(UUID.fromString("9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d"))
//                .build();

        // review
//        CreateReviewDto createReviewDto = CreateReviewDto.builder()
//                .customerId(this.customerRepository.findByUsername("hj1234").orElseThrow().getId())
//                .tellerId(this.tellerRepository.findTellerByUserName("dw1234").orElseThrow().getId())
//                .nps(10)
//                .consultingRoomId(UUID.fromString("9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d"))
//                .build();
//        this.consultingRoomService.createReview(createReviewDto);

    }
}
