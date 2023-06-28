package com.demo.productservice.service;

import com.demo.productservice.events.ProductCreatedEvent;
import com.demo.productservice.mapping.ProductMapper;
import com.demo.productservice.model.Product;
import com.demo.productservice.model.ProductRequest;
import com.demo.productservice.repository.OutboxRepository;
import com.demo.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Product createProduct(final ProductRequest productRequest) {
        final var productEntity = productRepository.save(productMapper.requestToEntity(productRequest));
        var productCreatedEvent = ProductCreatedEvent.of(productEntity);
        outboxRepository.save(productCreatedEvent);

        eventPublisher.publishEvent(productCreatedEvent);
        return productMapper.entityToDto(productEntity);
    }

}
