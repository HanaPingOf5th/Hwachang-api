package com.hwachang.hwachangapi.domain.tellerModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import com.hwachang.hwachangapi.domain.tellerModule.dto.ConsultingRoomResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueCustomerDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.QueueResponseDto;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerStatusRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import com.hwachang.hwachangapi.domain.tellerModule.service.WaitingQueueService;
import com.hwachang.hwachangapi.utils.apiPayload.ApiResponse;
import com.hwachang.hwachangapi.utils.apiPayload.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/queues")
@RequiredArgsConstructor
public class WaitingQueueController {

    private final WaitingQueueService waitingQueueService;
    private final TellerService tellerService;
    private final ConsultingRoomService consultingRoomService;

    @Operation(summary = "대기열에 고객 추가", description = "고객은 개인 금융 또는 기업 금융을 선택해 상담 대기실로 입장합니다.")
    @PostMapping("/{typeId}")
    public ApiResponse<Void> addCustomerToQueue(
            @PathVariable int typeId,
            @RequestParam(required = false) UUID categoryId) {
        String username = getCurrentUsername();
        boolean added = waitingQueueService.addCustomerToQueue(typeId, categoryId, username);
        return added
                ? ApiResponse.onSuccess(username + " 고객을 대기열에 추가하였습니다.", null)
                : ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "이미 대기열에 추가된 고객입니다.", null);
    }

    @Operation(summary = "대기열 정보 조회", description = "행원은 상담 하러 가기 페이지에서 대기열 정보를 조회합니다.")
    @GetMapping("/{typeId}/teller-entrance")
    public ApiResponse<QueueResponseDto> getWaitingQueuesInfo(@PathVariable int typeId) {
        QueueResponseDto responseDto = waitingQueueService.getWaitingQueuesInfo(typeId);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "행원 상담 후처리 요청", description = "행원이 상담을 마친 후 후처리 작업을 진행 중입니다.")
    @PostMapping("/teller-postprocessing")
    public ApiResponse<Void> requestTellerPostProcessing() {
        // 행원 상태 "다른 업무중"으로 변경
        TellerStatusRequestDto statusRequestDto = TellerStatusRequestDto.builder().status("BUSY").build();
        tellerService.updateStatus(statusRequestDto);

        return ApiResponse.onSuccess("행원이 다른 업무 중입니다.", null);
    }

    @Operation(summary = "다음 고객 처리", description = "대기열에서 다음 고객을 제거하고 처리합니다.")
    @DeleteMapping("/{typeId}/next")
    public ApiResponse<ConsultingRoomResponseDto> processNextCustomer(@PathVariable int typeId) {
        QueueCustomerDto nextCustomer = waitingQueueService.processNextCustomer(typeId);
        ConsultingRoomResponseDto responseDto = consultingRoomService.createConsultingRoom(nextCustomer.getCustomerId(), nextCustomer.getCategoryId());

        // 행원 상태 "상담 중"으로 변경
        TellerStatusRequestDto statusRequestDto = TellerStatusRequestDto.builder().status("UNAVAILABLE").build();
        tellerService.updateStatus(statusRequestDto);

        return nextCustomer != null
                ? ApiResponse.onSuccess(responseDto)
                : ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "대기열에 처리할 고객이 없습니다.", null);
    }

    @Operation(summary = "대기열에서 나가기", description = "현재 사용자가 대기열에서 자신을 제거합니다.")
    @DeleteMapping("/{typeId}")
    public ApiResponse<Void> leaveQueue(@PathVariable int typeId) {
        String username = getCurrentUsername();
        boolean removed = waitingQueueService.removeCustomerFromQueue(username, typeId);
        return removed
                ? ApiResponse.onSuccess("대기열에서 나갔습니다.", null)
                : ApiResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), "대기열을 찾을 수 없거나 비어있습니다.", null);
    }

    @Operation(summary = "대기열 크기 확인", description = "해당 대기열의 크기를 확인합니다.")
    @GetMapping("/{typeId}/size")
    public ApiResponse<Long> getQueueSize(@PathVariable int typeId) {
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