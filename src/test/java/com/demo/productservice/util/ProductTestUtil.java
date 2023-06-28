package com.demo.productservice.util;

import com.demo.productservice.domain.ProductEntity;
import com.demo.productservice.model.Product;
import com.demo.productservice.model.ProductRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductTestUtil {
    public static final Long ID = 1L;
    public static final String NAME = "Product 1";
    public static final BigDecimal PRICE = new BigDecimal("33.5");

    public static ProductEntity createProductEntity() {
        return new ProductEntity(ID, NAME, PRICE, LocalDateTime.now());
    }

    public static Product createProduct() {
        return new Product()
                .id(ID)
                .name(NAME)
                .price(PRICE);
    }

    public static ProductRequest createProductRequest() {
        return new ProductRequest()
                .name(NAME)
                .price(PRICE);
    }

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest()
                .name(name)
                .price(price);
    }
}