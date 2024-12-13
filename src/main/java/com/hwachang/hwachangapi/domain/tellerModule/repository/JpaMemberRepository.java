package com.hwachang.hwachangapi.domain.tellerModule.repository;

import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository{
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
    public Optional<TellerEntity> findMemberByUsername(String username) {
        TellerEntity member = em.createQuery("select m from TellerEntity m where m.username=:username", TellerEntity.class)
                .setParameter("username", username)
                .getSingleResult();
        return Optional.ofNullable(member);
    }
}
