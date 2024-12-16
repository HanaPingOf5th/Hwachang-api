package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class Subject {
    private String title;
    private List<Item> items;
}
