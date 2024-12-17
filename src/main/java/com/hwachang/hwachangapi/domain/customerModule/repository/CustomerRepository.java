package com.hwachang.hwachangapi.domain.customerModule.repository;

import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    void save(CustomerEntity member);

    void delete(CustomerEntity member);

    Optional<CustomerEntity> findById(UUID id);

    Optional<CustomerEntity> findByUsername(String username);
}