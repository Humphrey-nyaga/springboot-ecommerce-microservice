package com.humdev.productservice.controller;

import com.humdev.productservice.model.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ApiResponse<Product> createProduct(@RequestBody Product product) {

        Product newProduct = productService.createProduct(product);

        return ApiResponse.<Product>builder()
                .message("Product has been added successfully")
                .success(true)
                .data(newProduct)
                .build();
    }

    @GetMapping("/")
    public ApiResponse<List<Product>> listAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ApiResponse.<List<Product>>builder()
                .message("Products retrieved successfully")
                .success(true)
                .itemCount(products.size())
                .data(products)
                .build();
    }


}
