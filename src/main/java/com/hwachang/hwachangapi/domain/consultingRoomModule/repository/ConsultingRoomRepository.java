package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ConsultingRoomRepository {
    // @Query("SELECT c FROM ConsultingRoomEntity c WHERE :customerId MEMBER OF c.customerIds")
    //List<ConsultingRoomEntity> findByCustomerId(@Param("customerId") UUID customerId);
}
