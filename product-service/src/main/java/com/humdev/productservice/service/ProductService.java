package com.humdev.productservice.service;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest productCreateRequest);
   
    List<ProductResponse> createProductsInBatch(List<ProductCreateRequest> productsToCreateList);

    List<ProductResponse> findAllProducts();

    ProductResponse findProductById(Long productId);

    void deleteProductById(Long productId);

    List<BigDecimal> getProductsPrices(List<String> productCodes);
}
