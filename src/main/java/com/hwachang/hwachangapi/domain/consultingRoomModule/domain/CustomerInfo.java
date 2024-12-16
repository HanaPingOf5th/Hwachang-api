package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class CustomerInfo {
    private String name;
    private String residentNumber;
    private String address;
}
