package com.humdev.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humdev.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
