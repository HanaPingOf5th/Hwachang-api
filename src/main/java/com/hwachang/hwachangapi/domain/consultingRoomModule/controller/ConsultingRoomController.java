package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consulting-room")
@RequiredArgsConstructor
public class ConsultingRoomController {
    private final ConsultingRoomService consultingRoomService;

    @PostMapping("/end")
    public UUID endConsultingRoom(
            @RequestParam UUID consultingRoomId,
            @RequestParam UUID tellerId,
            @RequestParam UUID categoryId,
            @RequestParam List<UUID> customerIds,
            @RequestParam List<String> recordChat,
            @RequestParam String voiceUrl,
            @RequestParam String time) {
        // 상담 종료 후 데이터를 저장
        return consultingRoomService.saveConsultingRoomDetails(
                consultingRoomId, tellerId, categoryId, customerIds, recordChat, voiceUrl, time);
    }

    @PostMapping("/review")
    public UUID createReview(@RequestBody CreateReviewDto createReviewDto) {
        return this.consultingRoomService.createReview(createReviewDto);
    }
}
