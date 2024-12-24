package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.ConsultingListDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import com.hwachang.hwachangapi.domain.tellerModule.dto.TellerStatusRequestDto;
import com.hwachang.hwachangapi.domain.tellerModule.service.TellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consulting-room")
@RequiredArgsConstructor
public class ConsultingRoomController {
    private final ConsultingRoomService consultingRoomService;
    private final TellerService tellerService;

    @PostMapping("/end")
    public UUID endConsultingRoom(
            @RequestParam UUID consultingRoomId,
            @RequestParam UUID tellerId,
            @RequestParam UUID categoryId,
            @RequestParam List<UUID> customerIds,
            @RequestParam List<String> recordChat,
            @RequestParam String voiceUrl,
            @RequestParam String time) {

        // 행원 상태 "상담 가능"으로 변경
        TellerStatusRequestDto statusRequestDto = TellerStatusRequestDto.builder().status("AVAILABLE").build();
        tellerService.updateStatus(statusRequestDto);

        // 상담 종료 후 데이터를 저장
        return consultingRoomService.updateConsultingRoomDetails(
                consultingRoomId, tellerId, categoryId, customerIds, recordChat, voiceUrl, time);
    }

    @PostMapping("/review")
    public UUID createReview(@RequestBody CreateReviewDto createReviewDto) {
        return this.consultingRoomService.createReview(createReviewDto);
    }

    @GetMapping("/consulting-list")
    public List<ConsultingListDto> getConsultingList(@RequestParam UUID customerId) {
        List<ConsultingListDto> consultingListDtos = this.consultingRoomService.getConsultingList(customerId);
        return consultingListDtos;
    }
}
