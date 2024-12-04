package com.hwachang.hwachangapi.domain.member.repository;

import com.hwachang.hwachangapi.domain.member.entity.MemberEntity;
import com.hwachang.hwachangapi.domain.member.entity.TellerEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    void save(MemberEntity member);

    void delete(MemberEntity member);

    Optional<TellerEntity> findTellerById(Long id);

    Optional<MemberEntity> findMemberByUsername(String username);

}