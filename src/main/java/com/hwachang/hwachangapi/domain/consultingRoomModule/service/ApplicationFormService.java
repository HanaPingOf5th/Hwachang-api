package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.domain.ApplicationForm;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.SendApplicationFormDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationFormService {
    private final ApplicationFormRepository applicationFormRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public ApplicationForm getApplicationFormFromRedis() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String formId = valueOperations.get(username+"form");
        System.out.println(formId);

        return this.getApplicationForm(UUID.fromString(Objects.requireNonNull(formId)));
    }

    public void sendApplicationForm(SendApplicationFormDto dto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(dto.getUsername()+"form", String.valueOf(dto.getFormId()));
    }

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
