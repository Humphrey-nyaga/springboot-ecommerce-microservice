package com.humdev.productservice.service;

import java.util.List;

import com.humdev.productservice.model.CategoryCreateRequest;
import com.humdev.productservice.model.CategoryResponse;

import jakarta.validation.Valid;

public interface CategoryService {
    
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);

    CategoryResponse listById(Long id);

    List<CategoryResponse> list();
    
    List<CategoryResponse> createCategoriesInBatch(List<@Valid CategoryCreateRequest> categoryCreateRequest);

}
