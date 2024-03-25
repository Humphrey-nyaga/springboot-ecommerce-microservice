package com.humdev.productservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humdev.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductCodeIn(List<String> productCodes);
    Optional<Product> findByProductCode(String productCode);


}
