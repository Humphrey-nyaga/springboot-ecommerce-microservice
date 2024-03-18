package com.humdev.productservice.service;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductCreateResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest);

    List<Product> findAllProducts();

    Optional<ProductCreateResponse> findProductById(Long id);
}


