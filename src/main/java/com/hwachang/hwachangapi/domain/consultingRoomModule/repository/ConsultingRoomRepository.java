package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConsultingRoomRepository {

//    List<ConsultingRoomEntity> findAllByBankerId(UUID id);
    void save(ConsultingRoomEntity consultingRoomEntity);
    List<ConsultingRoomEntity> findAll();
    void deleteAll();
}
