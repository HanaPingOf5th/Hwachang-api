package com.hwachang.hwachangapi.domain.customerModule.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSignupRequestDto {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
}
