package com.humdev.productservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.TrueFalseConverter;

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
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_price")
    private BigDecimal price;

}
