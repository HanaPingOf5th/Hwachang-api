package com.hwachang.hwachangapi.domain.customerModule.repository;

import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import com.hwachang.hwachangapi.domain.tellerModule.dto.NpsDataDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    @Query("SELECT COALESCE(AVG(r.nps), 0) FROM ReviewEntity r WHERE r.tellerId = :tellerId")
    Integer findAverageNpsByTellerId(UUID tellerId);

    @Query("SELECT COUNT(r) FROM ReviewEntity r WHERE r.tellerId = :tellerId")
    Integer countCustomersByTellerId(UUID tellerId);

    @Query("SELECT r.content FROM ReviewEntity r WHERE r.tellerId = :tellerId")
    List<String> findReviewEntitiesByTellerId(UUID tellerId);

    @Query("SELECT new com.hwachang.hwachangapi.domain.tellerModule.dto.NpsDataDto(" +
            "CAST(COALESCE(SUM(CASE WHEN r.nps BETWEEN 9 AND 10 THEN 1 ELSE 0 END), 0) AS long), " +
            "CAST(COALESCE(SUM(CASE WHEN r.nps BETWEEN 7 AND 8 THEN 1 ELSE 0 END), 0) AS long), " +
            "CAST(COALESCE(SUM(CASE WHEN r.nps BETWEEN 0 AND 6 THEN 1 ELSE 0 END), 0) AS long)) " +
            "FROM ReviewEntity r " +
            "WHERE r.tellerId = :tellerId")
    NpsDataDto findNpsDataByTellerId(UUID tellerId);
}