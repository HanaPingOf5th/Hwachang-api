package com.hwachang.hwachangapi.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Diarization {
    private Boolean enabled;

    public Diarization(Boolean enabled) {
        this.enabled = enabled;
    }
}
