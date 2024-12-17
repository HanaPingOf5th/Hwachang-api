package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

}
