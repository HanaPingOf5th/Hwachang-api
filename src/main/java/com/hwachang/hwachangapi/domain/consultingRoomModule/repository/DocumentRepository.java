package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.DocumentByCategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    List<DocumentEntity> findAllByCategoryId(UUID categoryId);

    @Query("select new com.hwachang.hwachangapi.domain.consultingRoomModule.dto.DocumentByCategoryDto(d.documentId, d.title, d.path) "
            +"from DocumentEntity d "
            +"where d.title like %:keyword%")
    List<DocumentByCategoryDto> findAllByKeyword(@Param("keyword") String keyword);
}
