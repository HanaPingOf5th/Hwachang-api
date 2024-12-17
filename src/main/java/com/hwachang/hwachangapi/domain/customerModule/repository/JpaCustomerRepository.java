package com.hwachang.hwachangapi.domain.customerModule.repository;

import com.hwachang.hwachangapi.domain.customerModule.entities.CustomerEntity;
import com.hwachang.hwachangapi.domain.tellerModule.entities.TellerEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaCustomerRepository implements CustomerRepository {
    private final EntityManager em;

    @Override
    public void save(CustomerEntity member) {
        em.persist(member);
    }

    @Override
    public void delete(CustomerEntity member) {
        em.remove(member);
    }

    @Override
    public Optional<CustomerEntity> findById(UUID id) {
        CustomerEntity customer = em.find(CustomerEntity.class, id);
        return Optional.ofNullable(customer);
    }

    @Override
    public Optional<CustomerEntity> findByUsername(String username) {
        List<CustomerEntity> customers = em.createQuery(
                "select c from CustomerEntity c where c.username = :username", CustomerEntity.class)
                .setParameter("username", username).getResultList();
        return customers.stream().findFirst();
    }

    @Override
    public void deleteAll(){
        em.createQuery("delete from CustomerEntity").executeUpdate();
    }
}
