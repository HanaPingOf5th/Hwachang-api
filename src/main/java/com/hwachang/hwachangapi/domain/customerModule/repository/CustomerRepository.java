package com.hwachang.hwachangapi.domain.customerModule.repository;

import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByUsername(String username);
}