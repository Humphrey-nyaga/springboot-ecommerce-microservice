package com.humdev.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.humdev.productservice.model.ApiResponse;
import com.humdev.productservice.model.CategoryCreateRequest;
import com.humdev.productservice.model.CategoryResponse;
import com.humdev.productservice.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ApiResponse<List<CategoryResponse>> listAllCategories() {
        List<CategoryResponse> categoriesResponse = categoryService.list();

        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Categories retrieved successfully")
                .success(true)
                .itemCount(categoriesResponse.size())
                .data(categoriesResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> findCategory(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.listById(id);

        return ApiResponse.<CategoryResponse>builder()
                .message("Category retrieved successfully")
                .success(true)
                .itemCount(1)
                .data(categoryResponse)
                .build();
    }

    @PostMapping("/")
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryCreateRequest);

        return ApiResponse.<CategoryResponse>builder()
                .message("Category created successfully")
                .success(true)
                .itemCount(1)
                .data(categoryResponse)
                .build();
    }

    @PostMapping("/batchUpload")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResponse<List<CategoryResponse>> createInventory(
            @RequestBody List<@Valid CategoryCreateRequest> categoryCreateRequest) {

        List<CategoryResponse> categoriesResponse = categoryService.createCategoriesInBatch(categoryCreateRequest);
        ApiResponse<List<CategoryResponse>> response = ApiResponse.<List<CategoryResponse>>builder()
                .data(categoriesResponse)
                .message("Categories added successfully")
                .itemCount(categoriesResponse.size())
                .success(true)
                .build();
        return response;
    }

}
