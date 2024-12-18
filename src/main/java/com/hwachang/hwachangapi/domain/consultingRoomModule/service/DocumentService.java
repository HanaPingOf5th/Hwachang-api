package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.DocumentByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.DocumentEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public List<DocumentByCategoryDto> getAllDocumentsByCategoryId(UUID categoryId) {
        List<DocumentEntity> result = documentRepository.findAllByCategoryId(categoryId);
        List<DocumentByCategoryDto> documentList = new ArrayList<>();
        result.forEach(entity ->{
            DocumentByCategoryDto documentByCategoryDto = DocumentByCategoryDto.builder()
                    .documentId(entity.getDocumentId())
                    .title(entity.getTitle())
                    .path(entity.getPath())
                    .build();
            documentList.add(documentByCategoryDto);
        });
        return documentList;
    }

    public List<DocumentByCategoryDto> getAllDocumentsByKeyword(String keyword) {
        return documentRepository.findAllByKeyword(keyword);
    }
}
