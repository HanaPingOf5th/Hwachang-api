package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationFormRepository extends JpaRepository<ApplicationFormEntity, UUID> {
    ApplicationFormEntity findByApplicationFormId(UUID formId);

    List<ApplicationFormEntity> findAllByCategoryId(UUID categoryId);
}
