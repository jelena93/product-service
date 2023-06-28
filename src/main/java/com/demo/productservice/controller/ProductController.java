package com.demo.productservice.controller;

import com.demo.productservice.api.ProductApi;
import com.demo.productservice.model.Product;
import com.demo.productservice.model.ProductRequest;
import com.demo.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController implements ProductApi {
    private final ProductService productService;

    @Override
    public ResponseEntity<Product> createProduct(final ProductRequest productRequest) {
        final var product = productService.createProduct(productRequest);
        return ResponseEntity.ok(product);
    }
    
}
