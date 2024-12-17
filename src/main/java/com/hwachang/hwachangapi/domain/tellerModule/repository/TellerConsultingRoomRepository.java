package com.hwachang.hwachangapi.domain.tellerModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TellerConsultingRoomRepository extends JpaRepository<ConsultingRoomEntity, UUID> {

    @Query("select c.createdAt from ConsultingRoomEntity c " +
            "where c.bankerId = :id " +
            "and c.createdAt >= :startDate " +
            "and c.createdAt < :endDate")
    List<LocalDateTime> findAllByDate(UUID id, LocalDateTime startDate, LocalDateTime endDate);

    List<ConsultingRoomEntity> findAllByBankerId(UUID id);
}
