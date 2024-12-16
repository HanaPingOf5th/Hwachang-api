package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/consulting-room")
@RequiredArgsConstructor
public class ConsultingRoomControllerV2 {
    private final ConsultingRoomService consultingRoomService;

    @PostMapping("/review")
    public UUID createReview(CreateReviewDto createReviewDto) {
        return this.consultingRoomService.createReview(createReviewDto);
    }

}
