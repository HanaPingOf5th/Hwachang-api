package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationFormService {
    private final ApplicationFormRepository applicationFormRepository;

    public ApplicationForm getApplicationForm(UUID applicationFormId) {
        ApplicationFormEntity applicationFormEntity = applicationFormRepository.findByApplicationFormId(applicationFormId);
        return applicationFormEntity.getApplicationForm();
    }

    public List<FormByCategoryDto> getAllApplicationFormsByCategoryId(UUID categoryId) {
        List<ApplicationFormEntity> result = applicationFormRepository.findAllByCategoryId(categoryId);
        List<FormByCategoryDto> formList = new ArrayList<>();
        result.forEach(entity ->{
            FormByCategoryDto formByCategoryDto = FormByCategoryDto.builder()
                    .title(entity.getTitle())
                    .applicationFormId(entity.getApplicationFormId())
                    .build();
            formList.add(formByCategoryDto);
        });
        return formList;
    }

    public List<FormByCategoryDto> getAllFormsByKeyword(String keyword) {
        return applicationFormRepository.findAllByKeyword(keyword);
    }
}
