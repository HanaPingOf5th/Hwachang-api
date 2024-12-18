package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.DocumentByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/list/{categoryId}")
    public List<DocumentByCategoryDto> getAllDocumentsByCategoryId(@PathVariable("categoryId") UUID categoryId){
        return this.documentService.getAllDocumentsByCategoryId(categoryId);
    }
}
