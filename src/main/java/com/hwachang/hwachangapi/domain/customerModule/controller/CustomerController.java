package com.hwachang.hwachangapi.domain.customerModule.controller;

import com.hwachang.hwachangapi.domain.customerModule.dto.*;
import com.hwachang.hwachangapi.domain.customerModule.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/signup")
    public String signUp(@RequestBody CustomerSignupRequestDto customerSignupRequestDto) {
        return this.customerService.signup(customerSignupRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto signIn(@RequestBody LoginRequestDto loginRequestDto) {
        return this.customerService.login(loginRequestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // Authorization 헤더에서 "Bearer " 접두사를 제거
        token = token.replace("Bearer ", "");
        customerService.logout(token);

        return ResponseEntity.ok("로그아웃 성공. 클라이언트에서 토큰을 삭제하세요.");
    }

    @GetMapping("/consultings")
    public ResponseEntity<List<ConsultingListDto>> getConsultingRecords(
            @RequestParam(required = false) String summaryKeyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        List<ConsultingListDto> records = customerService.getCustomerConsultingRecords(summaryKeyword, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/consultings/detail/{consultingRoomId}")
    public ResponseEntity<ConsultingDetailsDto> getConsultingDetail(
            @PathVariable UUID consultingRoomId
    ) {
        ConsultingDetailsDto consultingDetail = customerService.getConsultingDetails(consultingRoomId);
        return ResponseEntity.ok(consultingDetail);
    }
}
