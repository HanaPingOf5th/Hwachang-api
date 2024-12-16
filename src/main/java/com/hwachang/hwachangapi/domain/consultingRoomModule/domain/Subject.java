package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class Subject implements Serializable {
    private String title;
    private List<Item> items;
}
