package com.hwachang.hwachangapi.domain.tellerModule.repository;

import com.hwachang.hwachangapi.domain.tellerModule.entities.Status;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<TellerEntity> findById(UUID tellerId) {
        TellerEntity teller = em.find(TellerEntity.class, tellerId);
        return Optional.ofNullable(teller);
    }

    @Override
    public Optional<TellerEntity> findTellerByUserName(String username) {
        List<TellerEntity> members = em.createQuery(
                        "select t from TellerEntity t where t.username = :username", TellerEntity.class)
                .setParameter("username", username)
                .getResultList();

        return members.stream().findFirst();
    }

    @Override
    public void deleteAll(){
        em.createQuery("delete from TellerEntity").executeUpdate();
    }

    @Override
    public long countByStatus(Status status) {
        Long count = em.createQuery(
                        "select count(t) from TellerEntity t where t.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult();
        return count;
    }
}
