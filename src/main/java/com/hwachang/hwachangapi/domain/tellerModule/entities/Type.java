package com.hwachang.hwachangapi.domain.tellerModule.entities;

public enum Type {
    PERSONAL("개인금융"),
    CORPORATE("기업금융");

    private final String description;

    Type(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}