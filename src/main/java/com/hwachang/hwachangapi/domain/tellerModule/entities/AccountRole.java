package com.hwachang.hwachangapi.domain.tellerModule.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountRole {
    Teller("ROLE_TELLER", "TELLER"),
    USER("ROLE_CUSTOMER", "CUSTOMER");

    private String key;
    private String name;
}