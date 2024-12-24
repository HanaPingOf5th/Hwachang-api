package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.ConsultingRoomEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
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

//    @Override
//    public Optional<UUID> findByCustomerId(UUID customerId) {
//        return Optional.ofNullable(em.createQuery("SELECT c.id FROM ConsultingRoomEntity c WHERE customerId in c.customerIds", ConsultingRoomEntity.class))
//    }
//    @Override
//    public Optional<UUID> findByCustomerId(UUID customerId) {
//        List<UUID> result = em.createQuery("SELECT c.id FROM ConsultingRoomEntity c WHERE :customerId MEMBER OF c.customerIds", UUID.class)
//                .setParameter("customerId", customerId)
//                .getResultList();
//        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
//    }
    @Override
    public Optional<UUID> findByCustomerId(UUID customerId) {
        // 'c.customerIds'가 List 같은 컬렉션 타입이라면, 'IN' 조건을 사용하는 방식으로 수정
        try {
            UUID result = em.createQuery("SELECT c.id FROM ConsultingRoomEntity c WHERE c.customerIds[0] = :customerId", UUID.class)
                    .setParameter("customerId", customerId)
                    .getSingleResult()
            return Optional.ofNullable(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
