package com.hwachang.hwachangapi.domain.tellerModule.repository;

import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaTellerRepository implements TellerRepository {
    private final EntityManager em;
    @Override
    public void save(TellerEntity member) {
        em.persist(member);
    }

    @Override
    public void delete(TellerEntity member) {
        em.remove(member);
    }

    @Override
    public Optional<TellerEntity> findTellerByUserName(String userName) {
        List<TellerEntity> members = em.createQuery(
                        "select t from TellerEntity t where t.userName = :userName", TellerEntity.class)
                .setParameter("userName", userName)
                .getResultList();

        return members.stream().findFirst();
    }

}
