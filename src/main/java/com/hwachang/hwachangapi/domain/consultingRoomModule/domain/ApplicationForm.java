package com.hwachang.hwachangapi.domain.consultingRoomModule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ApplicationForm implements Serializable {
    private UUID applicationFormId;
    private String title;
    public UUID categoryId;
    private CustomerInfo customerInfo;
    private List<Subject> subjects;

    public static ApplicationForm create(UUID applicationFormId,
                                         String title,
                                         UUID categoryId,
                                         CustomerInfo customerInfo,
                                         List<Subject> subjects) {

        return new ApplicationForm(applicationFormId, title, categoryId, customerInfo, subjects);
    }
}



