package com.humdev.productservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humdev.productservice.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    
}
