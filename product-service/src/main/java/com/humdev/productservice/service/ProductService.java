package com.humdev.productservice.service;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductCreateResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest);
   
    List<ProductCreateResponse> createProductsInBatch(List<ProductCreateRequest> productsToCreateList);

    List<Product> findAllProducts();

    ProductCreateResponse findProductById(Long productId);

    void deleteProductById(Long productId);

    List<BigDecimal> getProductsPrices(List<String> productCodes);
}
