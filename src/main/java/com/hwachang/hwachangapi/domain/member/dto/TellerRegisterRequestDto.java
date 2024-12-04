package com.hwachang.hwachangapi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TellerRegisterRequestDto {
    private String userName;
    private String name;
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }
}
