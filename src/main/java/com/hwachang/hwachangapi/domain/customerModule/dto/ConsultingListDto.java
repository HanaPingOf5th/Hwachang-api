package com.hwachang.hwachangapi.domain.customerModule.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ConsultingListDto {
    private String summary; // 상담 요약 (주제)
    private String tellerName; // 담당자 (행원이름)
    private String type; // 유형 (개인 or 기업)
    private String category; // 카테고리
    private LocalDateTime date; // 상담한 날짜
}
