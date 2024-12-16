package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.applicationForm.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ApplicationFormService {
    private final ApplicationFormRepository applicationFormRepository;

    public ApplicationForm getApplicationForm(UUID applicationFormId) {
        ApplicationFormEntity applicationFormEntity = applicationFormRepository.findByApplicationFormId(applicationFormId);
        return applicationFormEntity.getApplicationForm();
    }
}
