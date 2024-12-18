package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaConsultingRoomRepository implements ConsultingRoomRepository {
    private final EntityManager em;

    @Override
    public void save(ConsultingRoomEntity consultingRoomEntity) {
        em.persist(consultingRoomEntity);
    }

    @Override
    public List<ConsultingRoomEntity> findAll() {
        return em.createQuery("SELECT c FROM ConsultingRoomEntity c", ConsultingRoomEntity.class)
                .getResultList();
    }

    @Override
    public void deleteAll(){
        em.createQuery("DELETE FROM ConsultingRoomEntity").executeUpdate();
    }

    @Override
    public Optional<ConsultingRoomEntity> findById(UUID id) {
        return Optional.ofNullable(em.find(ConsultingRoomEntity.class, id));
    }
}
