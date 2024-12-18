package com.hwachang.hwachangapi.domain.customerModule.repository;

import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {
    @Query("SELECT COALESCE(AVG(r.nps), 0) FROM ReviewEntity r WHERE r.tellerId = :tellerId")
    Integer findAverageNpsByTellerId(UUID tellerId);

    @Query("SELECT COUNT(DISTINCT r.customerId) FROM ReviewEntity r WHERE r.tellerId = :tellerId")
    Integer countCustomersByTellerId(UUID tellerId);

    @Query("SELECT r.content FROM ReviewEntity r WHERE r.tellerId = :tellerId")
    List<String> findReviewEntitiesByTellerId(UUID tellerId);
}