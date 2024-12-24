package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ConsultingRoomRepository consultingRoomRepository;
    public List<CategoryDto> getCategories() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryEntities.forEach(categoryEntity -> {
            CategoryDto categoryDto = CategoryDto.builder()
                    .categoryId(categoryEntity.getCategoryId())
                    .categoryName(categoryEntity.getCategoryName())
                    .categoryType(categoryEntity.getCategoryType())
                    .build();
            categoryDtoList.add(categoryDto);
        });
        return categoryDtoList;
    }

    public List<CategoryDto> getCategoriesByCategoryType(Type categoryType) {
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByCategoryType(categoryType);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryEntities.forEach(categoryEntity -> {
            CategoryDto categoryDto = CategoryDto.builder()
                    .categoryId(categoryEntity.getCategoryId())
                    .categoryName(categoryEntity.getCategoryName())
                    .categoryType(categoryEntity.getCategoryType())
                    .build();
            categoryDtoList.add(categoryDto);
        });
        return categoryDtoList;
    }
}
