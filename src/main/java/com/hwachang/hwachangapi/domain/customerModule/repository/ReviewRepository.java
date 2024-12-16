package com.hwachang.hwachangapi.domain.customerModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.customerModule.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID> {

}