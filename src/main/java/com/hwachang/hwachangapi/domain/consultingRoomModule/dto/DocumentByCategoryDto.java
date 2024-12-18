package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DocumentByCategoryDto {
    private UUID documentId;
    private String title;
    private String path;
}
