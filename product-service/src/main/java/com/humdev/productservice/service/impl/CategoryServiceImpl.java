package com.humdev.productservice.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.humdev.productservice.entity.Category;
import com.humdev.productservice.exception.CategoryNotFoundException;
import com.humdev.productservice.model.CategoryCreateRequest;
import com.humdev.productservice.model.CategoryResponse;
import com.humdev.productservice.repository.CategoryRepository;
import com.humdev.productservice.service.CategoryService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryRepository.save(this.mapCategoryCreateRequestToCategory(categoryCreateRequest));
        return this.mapCategoryToCategoryResponse(category);
    }

    @Override
    public CategoryResponse listById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + id + " not found"));
        return this.mapCategoryToCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> list() {
        return categoryRepository.findAll().stream()
                .map(this::mapCategoryToCategoryResponse).toList();
    }

    private Category mapCategoryCreateRequestToCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryCreateRequest, category);
        return category;
    }

    private CategoryResponse mapCategoryToCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        BeanUtils.copyProperties(category, categoryResponse);
        return categoryResponse;
    }

    @Override
    public List<CategoryResponse> createCategoriesInBatch(List<@Valid CategoryCreateRequest> categoryCreateRequest) {
        List<Category> newCategories = categoryRepository.saveAll(categoryCreateRequest.stream()
                .map(this::mapCategoryCreateRequestToCategory)
                .toList());

        return newCategories.stream()
                .map(this::mapCategoryToCategoryResponse)
                .toList();
    }

}
