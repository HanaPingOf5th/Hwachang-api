package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ApplicationFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationFormController {
    private final ApplicationFormService applicationFormService;

    @GetMapping("/{formId}")
    public ApplicationForm getApplicationForm(@PathVariable("formId") UUID applicationFormId) {
        return this.applicationFormService.getApplicationForm(applicationFormId);
    }

    @GetMapping("/list/{categoryId}")
    public List<FormByCategoryDto> getAllApplicationFormsByCategoryId(@PathVariable("categoryId") UUID categoryId) {
        return this.applicationFormService.getAllApplicationFormsByCategoryId(categoryId);
    }
}
