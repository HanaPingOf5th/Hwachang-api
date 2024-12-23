package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ApplicationFormEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.SavedFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SavedFormRepository extends JpaRepository<SavedFormEntity, UUID> {

}
