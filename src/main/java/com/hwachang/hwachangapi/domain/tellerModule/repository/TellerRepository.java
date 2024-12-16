package com.hwachang.hwachangapi.domain.tellerModule.repository;

import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;

import java.util.Optional;
import java.util.UUID;

public interface TellerRepository {

    void save(TellerEntity member);

    void delete(TellerEntity member);

    Optional<TellerEntity> findById(UUID id);

    Optional<TellerEntity> findTellerByUserName(String username);

}