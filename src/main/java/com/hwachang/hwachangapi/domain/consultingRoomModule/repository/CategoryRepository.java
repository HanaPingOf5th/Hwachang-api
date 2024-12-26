package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<CategoryEntity> findById(UUID id);
    List<CategoryEntity> findAll();
    void save(CategoryEntity categoryEntity);

    List<CategoryEntity> findAllByCategoryType(Type categoryType);
}
