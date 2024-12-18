package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CreateReviewDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.DocumentByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ApplicationFormService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.CategoryService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.ConsultingRoomService;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/consulting-room")
@RequiredArgsConstructor
public class ConsultingRoomController {
    private final ConsultingRoomService consultingRoomService;
    private final ApplicationFormService applicationFormService;
    private final CategoryService categoryService;
    private final DocumentService documentService;

    @PostMapping("/review")
    public UUID createReview(@RequestBody CreateReviewDto createReviewDto) {
        return this.consultingRoomService.createReview(createReviewDto);
    }

    @GetMapping("/application/{formId}")
    public ApplicationForm getApplicationForm(@PathVariable("formId") UUID applicationFormId) {
        return this.applicationFormService.getApplicationForm(applicationFormId);
    }

    @GetMapping("/application/form-list/{categoryId}")
    public List<FormByCategoryDto> getAllApplicationFormsByCategoryId(@PathVariable("categoryId") UUID categoryId) {
        return this.applicationFormService.getAllApplicationFormsByCategoryId(categoryId);
    }

    @GetMapping("/document/document-list/{categoryId}")
    public List<DocumentByCategoryDto> getAllDocumentsByCategoryId(@PathVariable("categoryId") UUID categoryId){
        return this.documentService.getAllDocumentsByCategoryId(categoryId);
    }

    @GetMapping("/application/categories")
    public List<CategoryDto> getCategories() {
        return this.categoryService.getCategories();
    }
}
