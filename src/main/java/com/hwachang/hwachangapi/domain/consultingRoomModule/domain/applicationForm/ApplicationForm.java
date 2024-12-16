package com.hwachang.hwachangapi.domain.consultingRoomModule.domain.applicationForm;

import java.util.List;
import java.util.UUID;

public class ApplicationForm {
    private UUID applicationFormId;
    private String title;
    public UUID categoryId;
    private CustomerInfo customerInfo;
    private List<Subject> subjects;
}

