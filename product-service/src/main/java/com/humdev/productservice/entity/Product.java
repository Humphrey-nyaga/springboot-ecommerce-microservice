package com.humdev.productservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "products",
uniqueConstraints = {
    @UniqueConstraint(columnNames = "product_code", name = "UNIQUE_PRODUCT_CODE")
})
@NoArgsConstructor
@Data
@AllArgsConstructor
@SoftDelete(columnName = "deleted", converter = TrueFalseConverter.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    @NonNull
    private String name;

    @Column(name = "product_code")
    @NonNull
    private String productCode;

    @Column(name = "product_price")
    @Min(value = 1, message = "Product Price cannot be less than 1")
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
