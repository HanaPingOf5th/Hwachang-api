package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ApplicationForm implements Serializable {
    private String title;
    private CustomerInfo customerInfo;
    private List<Subject> subjects;

    public static ApplicationForm create(String title, UUID categoryId, CustomerInfo customerInfo, List<Subject> subjects) {
        return new ApplicationForm(title, customerInfo, subjects);
    }
}



