package com.humdev.productservice.controller;

import com.humdev.productservice.model.ApiResponse;

import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ApiResponse<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {

        ProductCreateResponse newProduct = productService.createProduct(productCreateRequest);
        System.out.println(":::::::::::::::ProductCreateResponse:::::_>  " + newProduct);

        return ApiResponse.<ProductCreateResponse>builder()
                .message("Product has been added successfully")
                .success(true)
                .itemCount(1)
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

    @GetMapping("/{productId}")
    public ApiResponse<ProductCreateResponse> getProductById(@PathVariable Long productId) {
        ProductCreateResponse product = productService.findProductById(productId);

        return ApiResponse.<ProductCreateResponse>builder()
                .message("Product retrieved successfully")
                .success(true)
                .itemCount(1)
                .data(product)
                .build();
        // }return ApiResponse.<ProductCreateResponse>builder().message("Product not
        // found").success(false).itemCount(0).build();
    }

}
