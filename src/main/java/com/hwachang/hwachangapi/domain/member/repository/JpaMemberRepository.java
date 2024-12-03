package com.hwachang.hwachangapi.domain.member.repository;

import com.hwachang.hwachangapi.domain.member.entity.MemberEntity;
import com.hwachang.hwachangapi.domain.member.entity.TellerEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;
    @Override
    public void save(MemberEntity member) {
        em.persist(member);
    }

    @Override
    public void delete(MemberEntity member) {
        em.remove(member);
    }

    @Override
    public Optional<TellerEntity> findTellerById(Long id) {
        try{
            TellerEntity teller = em.find(TellerEntity.class, id);
            return Optional.ofNullable(teller);
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Optional<MemberEntity> findMemberByUsername(String username) {
        return Optional.empty();
    }
}
