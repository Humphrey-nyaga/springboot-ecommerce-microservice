package com.humdev.productservice.service;

import com.humdev.productservice.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> findAllProducts();

    Optional<Product> findProductById(Long id);
}


