package com.humdev.productservice.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.humdev.productservice.model.ProductCreateRequest;
import com.humdev.productservice.model.ProductResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humdev.productservice.entity.Category;
import com.humdev.productservice.entity.Product;
import com.humdev.productservice.exception.ProductNotFoundException;
import com.humdev.productservice.repository.ProductRepository;
import com.humdev.productservice.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        Product product = productRepository.save(this.mapProductRequestToProduct(productCreateRequest));
        System.out.println(":::::::::::::::::Product is-> " + product);
        return mapProductToProductResponse(product);

    }

    private Product mapProductRequestToProduct(ProductCreateRequest productCreateRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(productCreateRequest, product);

        Category category = new Category();
        category.setId(productCreateRequest.getCategoryId());
        product.setCategory(category);
        System.out.println(":::::::::::::::::Product is-> " + product);

        return product;
    }

    private ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        if (product.getCategory() != null) {
            productResponse.setCategoryId(product.getCategory().getId());
        }
        return productResponse;
    }

    @Override
    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll().stream()
        .map(this::mapProductToProductResponse).toList();
    }

    @Override
    public ProductResponse findProductById(Long id) {
        Optional<Product> foundProduct = productRepository.findById(id);
        if (foundProduct.isPresent()) {
            return mapProductToProductResponse(foundProduct.get());
        } else {
            throw new ProductNotFoundException("Product with id " + id + " does not exist.");
        }
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<BigDecimal> getProductsPrices(List<String> productCodes) {
        List<BigDecimal> productsPrices = new ArrayList<>();
        Set<String> unavailableProducts = new HashSet<>();

        for (String productCode : productCodes) {

            Product product = productRepository.findByProductCode(productCode).orElse(null);
            if (product != null) {
                productsPrices.add(product.getPrice());
            } else {
                unavailableProducts.add(productCode);
            }
        }
        if (unavailableProducts.isEmpty()) {
            log.info("::::::::::::Service Retrieved Product Prices Successfully ::::::::::::::::::::::: "
                    + productsPrices);

            return productsPrices;

        } else {
            throw new ProductNotFoundException("The Product(s) with the productcodes given do not exist",
                    unavailableProducts);

        }

    }

    @Override
    public List<ProductResponse> createProductsInBatch(List<ProductCreateRequest> productsToCreateList) {

        List<Product> newProducts = productRepository.saveAll(productsToCreateList.stream()
                .map(this::mapProductRequestToProduct)
                .toList());

        return newProducts.stream()
                .map(this::mapProductToProductResponse)
                .toList();
    }

}
