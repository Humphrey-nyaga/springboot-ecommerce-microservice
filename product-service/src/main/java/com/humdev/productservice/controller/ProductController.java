package com.humdev.productservice.controller;

import com.humdev.productservice.model.ApiResponse;

import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.service.ProductService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

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

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProductById(@PathVariable Long productId) {
        productService.deleteProductById(productId);

        return ApiResponse.<String>builder()
                .message("Product deleted successfully")
                .success(true)
                .build();
    }

    // getProductsPrices(List<String> productCodes)
    @GetMapping("/productPrices")
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<List<BigDecimal>> getProductsPrices(
            @RequestParam("productCodes") List<String> productCodes){

        log.info("::::::::::::Products Controller To Get Item Prices:::::::::::::::::::::::");

        List<BigDecimal> productPrices = productService.getProductsPrices(productCodes);
        log.info("::::::::::::Returned Product Prices::::::::::::::::::::::: " + productPrices);

        ApiResponse<List<BigDecimal>> response = ApiResponse.<List<BigDecimal>>builder()
                .data(productPrices)
                .message("Prices retrieved successfully")
                .success(true)
                .build();
        return response;
    }

    // TODO Batch uploads inserting one value at a time. Batch insert needs to be enabled in properties
    @PostMapping("/batchUpload")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResponse<List<ProductCreateResponse>> createInventory(
            @RequestBody List<@Valid ProductCreateRequest> productsCreateRequests) {
                
        List<ProductCreateResponse> newProducts = productService.createProductsInBatch(productsCreateRequests);
        ApiResponse<List<ProductCreateResponse>> response = ApiResponse.<List<ProductCreateResponse>>builder()
                .data(newProducts)
                .message("Products added successfully")
                .itemCount(newProducts.size())
                .success(true)
                .build();
        return response;
    }
}
