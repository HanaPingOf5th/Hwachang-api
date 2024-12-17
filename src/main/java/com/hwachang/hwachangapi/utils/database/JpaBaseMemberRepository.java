package com.hwachang.hwachangapi.utils.database;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBaseMemberRepository implements BaseMemberRepository {
    private final EntityManager em;

    @Override
    public Optional<BaseMemberEntity> findMemberByUsername(String username) {
        BaseMemberEntity baseMemberEntity = em.createQuery(
                "select m from BaseMemberEntity m where m.username=:username", BaseMemberEntity.class
        ).setParameter("username", username).getSingleResult();
        return Optional.ofNullable(baseMemberEntity);
    }
}
