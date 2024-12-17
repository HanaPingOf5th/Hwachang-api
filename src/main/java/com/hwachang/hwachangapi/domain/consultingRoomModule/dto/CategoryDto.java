package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CategoryDto {
    private UUID categoryId;
    private String categoryName;
}
