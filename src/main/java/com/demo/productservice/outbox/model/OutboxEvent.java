package com.demo.productservice.outbox.model;

import java.util.UUID;

public interface OutboxEvent {
    String getAggregateId();

    String getAggregateType();

    String getPayload();

    String getType();

    UUID getId();
}
