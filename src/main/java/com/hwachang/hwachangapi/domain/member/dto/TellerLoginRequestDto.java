package com.hwachang.hwachangapi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TellerLoginRequestDto {
    private String tellerId;
    private String password;
}
