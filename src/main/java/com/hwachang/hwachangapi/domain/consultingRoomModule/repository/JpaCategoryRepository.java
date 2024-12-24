package com.hwachang.hwachangapi.domain.consultingRoomModule.repository;

import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class JpaCategoryRepository implements CategoryRepository {
    private final EntityManager em;

    @Override
    public Optional<CategoryEntity> findById(UUID id){
        return Optional.ofNullable(em.find(CategoryEntity.class, id));
    }

    @Override
    public List<CategoryEntity> findAll(){
        return em.createQuery("select c from CategoryEntity c", CategoryEntity.class).getResultList();
    }

    @Override
    public void save(CategoryEntity categoryEntity) {
        em.persist(categoryEntity);
    }
}
