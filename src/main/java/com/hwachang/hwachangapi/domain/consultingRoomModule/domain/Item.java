package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Item {
    private String type;
    private String description;
}
