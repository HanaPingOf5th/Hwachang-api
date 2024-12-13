package com.hwachang.hwachangapi.domain.member.repository;

import com.hwachang.hwachangapi.domain.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository {

    void save(MemberEntity member);

    void delete(MemberEntity member);

    Optional<MemberEntity> findMemberByUsername(String username);

}