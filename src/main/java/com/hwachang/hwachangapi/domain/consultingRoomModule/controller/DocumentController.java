package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.DocumentByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public List<DocumentByCategoryDto> getAllDocumentsByKeyword(@RequestParam("keyword") String keyword){
        return this.documentService.getAllDocumentsByKeyword(keyword);
    }
}
