package com.hwachang.hwachangapi.domain.consultingRoomModule.service;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.entities.CategoryEntity;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.CategoryRepository;
import com.hwachang.hwachangapi.domain.consultingRoomModule.repository.ConsultingRoomRepository;
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
                    .build();
            categoryDtoList.add(categoryDto);
    });
        return categoryDtoList;
    }
}
