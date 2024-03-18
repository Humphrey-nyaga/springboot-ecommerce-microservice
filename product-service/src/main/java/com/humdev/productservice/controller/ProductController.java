package com.humdev.productservice.controller;

import com.humdev.productservice.model.ApiResponse;

import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/products")
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

    @GetMapping("/{id}")
    public ApiResponse<ProductCreateResponse> getProductById(@PathVariable Long id) {
        Optional<ProductCreateResponse> product = productService.findProductById(id);

        if (product.isPresent()) {
            return ApiResponse.<ProductCreateResponse>builder()
                    .message("Products retrieved successfully")
                    .success(true)
                    .itemCount(1)
                    .data(product.get())
                    .build();
        }
        return ApiResponse.<ProductCreateResponse>builder()
                .message("Product not found")
                .success(false)
                .itemCount(0)
                .build();
    }


}
