package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.domain.tellerModule.service.WaitingQueueService;
import com.hwachang.hwachangapi.utils.apiPayload.ApiResponse;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/queues")
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;

    @Operation(summary = "고객 대기열 추가", description = "고객은 개인 금융 또는 기업 금융을 선택해 상담 대기실로 입장합니다.")
    @PostMapping("/{typeId}")
    public ApiResponse<Void> addToQueue(@PathVariable String typeId) {
        String username = getCurrentUsername();
        waitingQueueService.addCustomerToQueue(username, typeId);

        return ApiResponse.onSuccess(username + " 고객을 대기열에 추가하였습니다.", null);
    }

    @Operation(summary = "다음 고객 처리", description = "대기열에서 다음 고객을 제거하고 처리합니다.")
    @DeleteMapping("/{typeId}/next")
    public ApiResponse<String> processNextCustomer(@PathVariable String typeId) {
        String nextCustomer = waitingQueueService.processNextCustomer(typeId);
        return nextCustomer != null
                ? ApiResponse.onSuccess(nextCustomer + " 고객을 처리했습니다.")
                : ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "대기열에 처리할 고객이 없습니다.", null);
    }

    @Operation(summary = "대기열에서 나가기", description = "현재 사용자가 대기열에서 자신을 제거합니다.")
    @DeleteMapping("/{typeId}")
    public ApiResponse<Void> leaveQueue(@PathVariable String typeId) {
        String username = getCurrentUsername();
        waitingQueueService.removeCustomerFromQueue(username, typeId);

        return ApiResponse.onSuccess("대기열에서 나갔습니다.", null);
    }

    @Operation(summary = "대기열 크기 확인", description = "해당 대기열의 크기를 확인합니다.")
    @GetMapping("/{typeId}/size")
    public ApiResponse<Long> getQueueSize(@PathVariable String typeId) {
        Long size = waitingQueueService.getWaitingQueueSize(typeId);
        return ApiResponse.onSuccess(size);
    }

    // 현재 로그인된 사용자 가져오기
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 비어 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        // username 추출
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("유효하지 않은 사용자 정보입니다.");
        }
    }
}
