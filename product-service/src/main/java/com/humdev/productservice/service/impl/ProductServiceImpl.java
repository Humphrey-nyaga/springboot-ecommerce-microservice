package com.humdev.productservice.service.impl;

import java.util.List;
import java.util.Optional;

import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductCreateResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humdev.productservice.entity.Product;
import com.humdev.productservice.exception.ProductNotFoundException;
import com.humdev.productservice.repository.ProductRepository;
import com.humdev.productservice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest) {
        Product product = productRepository.save(this.mapProductRequestToProduct(productCreateRequest));
        System.out.println(":::::::::::::::::Product is-> " + product);
        return mapProductToProductResponse(product);

    }

    private Product mapProductRequestToProduct(ProductCreateRequest productCreateRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(productCreateRequest, product);
        System.out.println(":::::::::::::::::Product is-> " + product);

        return product;
    }

    private ProductCreateResponse mapProductToProductResponse(Product product) {
        ProductCreateResponse productCreateResponse = new ProductCreateResponse();
        BeanUtils.copyProperties(product, productCreateResponse);
        return productCreateResponse;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductCreateResponse findProductById(Long id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        if (foundProduct.isPresent()) {
            return mapProductToProductResponse(foundProduct.get());
        } else {
            throw new ProductNotFoundException("Product with id " + id + " does not exist.");
        }
    }

}
