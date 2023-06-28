package com.demo.productservice.outbox.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@ToString
public class Outbox implements OutboxEvent {

    private UUID id;
    private String aggregateType;
    private String aggregateId;
    private String type;
    private String payload;
    private LocalDateTime createdAt;
    
}
