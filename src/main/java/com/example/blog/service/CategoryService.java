package com.example.blog.service;

import com.example.blog.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long id);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategory();

    void deleteCategory(Long id);
}
