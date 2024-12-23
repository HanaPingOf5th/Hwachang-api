package com.hwachang.hwachangapi.domain.consultingRoomModule.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ConsultingListDto {
    private UUID consultingRoomId;    // 컨설팅룸 ID
    private LocalDateTime createdAt;
    private String title;
    private String categoryName;
    private String summary;
}
