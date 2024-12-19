package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class DocumentByCategoryDto {
    private UUID documentId;
    private String title;
    private String path;
}
