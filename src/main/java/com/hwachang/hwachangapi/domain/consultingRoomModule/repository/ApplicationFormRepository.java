package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplicationFormRepository extends JpaRepository<ApplicationFormEntity, UUID> {
    ApplicationFormEntity findByApplicationFormId(UUID formId);

    List<ApplicationFormEntity> findAllByCategoryId(UUID categoryId);

    @Query("select new com.hwachang.hwachangapi.domain.consultingRoomModule.dto.FormByCategoryDto(f.applicationFormId, f.title) "
            +"from ApplicationFormEntity f "
            +"where f.title like %:keyword%")
    List<FormByCategoryDto> findAllByKeyword(String keyword);

    @Modifying
    @Query(value = "INSERT INTO application_form (application_form_id, category_id, title, application_form) " +
            "VALUES (:applicationFormId, :categoryId, :title, CAST(:applicationForm AS jsonb));",
            nativeQuery = true)
    void createApplicationFormEntity(
            @Param("applicationFormId") UUID applicationFormId,
            @Param("categoryId") UUID categoryId,
            @Param("title") String title,
            @Param("applicationForm") String applicationForm);



}
