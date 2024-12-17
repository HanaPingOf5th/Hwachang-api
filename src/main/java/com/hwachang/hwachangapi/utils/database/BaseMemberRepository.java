package com.hwachang.hwachangapi.utils.database;

import java.util.Optional;

public interface BaseMemberRepository {
    Optional<BaseMemberEntity> findMemberByUsername(String username);
}
