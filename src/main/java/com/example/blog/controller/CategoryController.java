package com.example.blog.controller;

import com.example.blog.dto.ApiResponse;
import com.example.blog.dto.category.CategoryDto;
import com.example.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    ResponseEntity<List<CategoryDto>> getAllCategory() {
        return ResponseEntity.ok(this.categoryService.getAllCategory());
    }

    @GetMapping("{id}")
    ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") long categoryId) {
        return ResponseEntity.ok(this.categoryService.getCategoryById(categoryId));
    }

    @PostMapping("")
    ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    ResponseEntity<CategoryDto> updateCategory(@Valid @PathVariable("id") long categoryId, @RequestBody CategoryDto categoryDto) {
        CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(updateCategory);
    }

    @DeleteMapping("{id}")
    ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") long categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse(200, true, "Category Deleted Successfully"), HttpStatus.OK);
    }
}
