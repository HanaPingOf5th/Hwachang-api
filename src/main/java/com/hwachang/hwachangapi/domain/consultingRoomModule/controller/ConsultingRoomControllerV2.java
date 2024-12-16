package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.applicationForm.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ApplicationFormService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/consulting-room")
@RequiredArgsConstructor
public class ConsultingRoomControllerV2 {
    private final ConsultingRoomService consultingRoomService;
    private final ApplicationFormService applicationFormService;

    @PostMapping("/review")
    public UUID createReview(@RequestBody CreateReviewDto createReviewDto) {
        return this.consultingRoomService.createReview(createReviewDto);
    }

    @GetMapping("/application/{formId}")
    public ApplicationForm getApplicationForm(@PathVariable("formId") UUID applicationFormId) {
        return this.applicationFormService.getApplicationForm(applicationFormId);
    }

}
