package com.hwachang.hwachangapi.utils.database;

import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ApplicationFormRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.customerModule.dto.CustomerSignupRequestDto;
import com.hwachang.hwachangapi.domain.customerModule.repository.CustomerRepository;
import com.hwachang.hwachangapi.domain.customerModule.repository.ReviewRepository;
import com.hwachang.hwachangapi.domain.customerModule.service.CustomerService;
import com.hwachang.hwachangapi.domain.tellerModule.dto.CreateTellerRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.repository.TellerRepository;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final TellerRepository tellerRepository;
    private final CustomerRepository customerRepository;
    private final TellerService tellerService;
    private final CustomerService customerService;

    private final ApplicationFormRepository applicationFormRepository;
    private final CategoryRepository categoryRepository;
    private final ConsultingRoomRepository consultingRoomRepository;
    private final ReviewRepository reviewRepository;

    public DatabaseSeeder(TellerRepository tellerRepository, CustomerRepository customerRepository, TellerService tellerService, CustomerService customerService, ApplicationFormRepository applicationFormRepository, CategoryRepository categoryRepository, ConsultingRoomRepository consultingRoomRepository, ReviewRepository reviewRepository) {
        this.tellerRepository = tellerRepository;
        this.customerRepository = customerRepository;
        this.tellerService = tellerService;
        this.customerService = customerService;
        this.applicationFormRepository = applicationFormRepository;
        this.categoryRepository = categoryRepository;
        this.consultingRoomRepository = consultingRoomRepository;
        this.reviewRepository = reviewRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        CreateTellerRequestDto createTellerRequestDto = CreateTellerRequestDto.builder().tellerNumber("hj1234").name("함형주").position("대리").password("12345").build();
        this.tellerService.signup(createTellerRequestDto);
        CustomerSignupRequestDto customerSignupRequestDto = CustomerSignupRequestDto.builder().username("dw1234").name("김동은").phoneNumber("010-1111-1111").password("12345").build();
    }
}
