package com.hwachang.hwachangapi.domain.tellerModule.entities;

public enum Status {
    AVAILABLE("상담가능"),
    BUSY("다른 업무중"),
    UNAVAILABLE("상담 불가");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}