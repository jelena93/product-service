package com.demo.productservice.events;

import com.demo.productservice.domain.ProductEntity;
import com.demo.productservice.outbox.model.OutboxEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@ToString
public class ProductCreatedEvent implements OutboxEvent {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final long productId;
    private final UUID uuid;
    private final String product;

    @Override
    public String getAggregateId() {
        return String.valueOf(productId);
    }

    @Override
    public String getAggregateType() {
        return "product";
    }

    @Override
    public String getPayload() {
        return product;
    }

    @Override
    public String getType() {
        return EventType.PRODUCT.name();
    }

    @Override
    public UUID getId() {
        return uuid;
    }

    public static OutboxEvent of(final ProductEntity product) {
        final var productJson = MAPPER.createObjectNode()
                .put("id", product.getId())
                .put("name", product.getName())
                .put("price", product.getPrice())
                .put("createdAt", product.getCreatedAt().toString());

        return new ProductCreatedEvent(product.getId(), UUID.randomUUID(), productJson.toString());
    }
}
