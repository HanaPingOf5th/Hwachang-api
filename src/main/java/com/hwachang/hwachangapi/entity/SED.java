package com.hwachang.hwachangapi.entity;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SED {
    private Boolean enabled;

    public SED(Boolean enabled) {
        this.enabled = enabled;
    }
}
