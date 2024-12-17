package com.hwachang.hwachangapi.domain.customerModule.dto;


import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Builder
public class ConsultingListDto {
    private String summary;       // 상담 요약 (주제)
    private String tellerName;    // 담당자 (행원 이름)
    private String type;          // 유형 (개인금융 or 기업금융)
    private String category;      // 카테고리
    private LocalDateTime date;   // 상담한 날짜

    // Type Enum을 문자열로 변환하는 메서드
    public static String mapTypeToString(Type type) {
        return type != null ? type.getDescription() : null;
    }
}
