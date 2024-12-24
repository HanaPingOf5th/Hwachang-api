package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CategoryDto {
    private UUID categoryId;
    private String categoryName;
    private Type categoryType;
}
