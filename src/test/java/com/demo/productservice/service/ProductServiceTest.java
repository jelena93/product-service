package com.demo.productservice.service;


import com.demo.productservice.mapping.ProductMapper;
import com.demo.productservice.outbox.model.OutboxEvent;
import com.demo.productservice.repository.OutboxRepository;
import com.demo.productservice.repository.ProductRepository;
import com.demo.productservice.util.ProductTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OutboxRepository outboxRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ProductService productService;
    @Captor
    private ArgumentCaptor<OutboxEvent> outboxArgumentCaptor;

    @BeforeEach
    public void setup() {
        productService = new ProductService(productRepository, productMapper, outboxRepository, eventPublisher);
    }

    @Test
    public void testCreateProduct() {
        final var productRequest = ProductTestUtil.createProductRequest();
        final var productEntity = ProductTestUtil.createProductEntity();
        final var product = ProductTestUtil.createProduct();

        when(productMapper.requestToEntity(productRequest)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        when(productMapper.entityToDto(productEntity)).thenReturn(product);

        var result = productService.createProduct(productRequest);

        verify(outboxRepository).save(outboxArgumentCaptor.capture());
        verify(eventPublisher).publishEvent(any(OutboxEvent.class));

        assertThat(product).isEqualTo(result);

        assertThat(outboxArgumentCaptor.getValue())
                .extracting(OutboxEvent::getType, OutboxEvent::getAggregateType)
                .containsExactly("PRODUCT", "product");
    }

}