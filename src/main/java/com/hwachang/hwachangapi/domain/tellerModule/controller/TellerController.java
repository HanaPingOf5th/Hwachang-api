package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.domain.tellerModule.dto.*;
import com.hwachang.hwachangapi.domain.tellerModule.service.HwaChangLogService;
import com.hwachang.hwachangapi.utils.apiPayload.ApiResponse;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teller")
@RequiredArgsConstructor
public class TellerController {
    private static final Logger log = LogManager.getLogger(TellerController.class);
    private final TellerService tellerService;
    private final HwaChangLogService hwaChangLogService;

    @PostMapping("/signup")
    public String signUp(@RequestBody CreateTellerRequestDto createTellerRequestDto) {
        log.info("행원 회원가입 " + createTellerRequestDto.getName());
        return this.tellerService.signup(createTellerRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto signIn(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("행원 로그인 " + loginRequestDto.getTellerNumber());
        return this.tellerService.login(loginRequestDto);
    }

    @Operation(summary = "행원 상담 현황 조회", description = "행원은 메인 페이지에서 대시보드로 상담 현황을 확인할 수 있습니다.")
    @GetMapping("/main")
    public ApiResponse<TellerMainResponse> getTellerDashboard() {
        TellerMainResponse responseDto = hwaChangLogService.getTellerDashboardData();
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "행원 정보 조회", description = "행원은 우측 사이드바에서 행원의 프로필을 확인할 수 있습니다.")
    @GetMapping("/mypage")
    public ApiResponse<TellerInfoResponseDto> getTellerInfo() {
        TellerInfoResponseDto responseDto = tellerService.getTellerInfo();
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "행원 상태 변경", description = "행원은 상담 가능, 다른 업무중, 상담 불가, 업무 종료 중 상태를 변경할 수 있습니다.")
    @PatchMapping("/status")
    public ApiResponse<Void> changeTellerStatus(@RequestBody TellerStatusRequestDto statusRequestDto) {
        tellerService.updateStatus(statusRequestDto);
        return ApiResponse.onSuccess("상태 변경에 성공하였습니다.", null);
    }
}
