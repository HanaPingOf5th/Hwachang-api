package com.hwachang.hwachangapi.domain.consultingRoomModule.controller;

import com.hwachang.hwachangapi.domain.consultingRoomModule.dto.CategoryDto;
import com.hwachang.hwachangapi.domain.consultingRoomModule.service.CategoryService;
import com.hwachang.hwachangapi.domain.tellerModule.entities.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategories() {
        return this.categoryService.getCategories();
    }

    @GetMapping("/{categoryType}")
    public List<CategoryDto> getCategoriesByCategoryType(@PathVariable Type categoryType) {
        return this.categoryService.getCategoriesByCategoryType(categoryType);
    }
}