package com.demo.productservice.repository;

import com.demo.productservice.outbox.model.Outbox;
import com.demo.productservice.outbox.model.OutboxEvent;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OutboxRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void save(final OutboxEvent outboxEvent) {
        String insertQuery = "INSERT INTO outbox (id, aggregate_id, aggregate_type, type, payload) " +
                "VALUES (:id, :aggregateId, :aggregateType, :type, CAST(:payload AS JSONB))";

        namedParameterJdbcTemplate.update(insertQuery, Map.of(
                "id", outboxEvent.getId(),
                "aggregateId", outboxEvent.getAggregateId(),
                "aggregateType", outboxEvent.getAggregateType(),
                "type", outboxEvent.getType(),
                "payload", outboxEvent.getPayload()
        ));
    }

    public void deleteById(final UUID id) {
        String deleteQuery = "DELETE FROM outbox WHERE id = :id";
        namedParameterJdbcTemplate.update(deleteQuery, Map.of("id", id));
    }

    public void deleteByIds(final List<Outbox> ids) {
        if (ids.isEmpty()) {
            return;
        }
        String deleteQuery = "DELETE FROM outbox WHERE id in (:ids)";

        namedParameterJdbcTemplate.update(deleteQuery, Map.of("ids", 
                ids.stream()
                .map(Outbox::getId)
                .collect(Collectors.toList())));
    }

    public List<Outbox> fetchUnsent(int limit) {
        final var query = "select * from outbox where created_at < (current_timestamp - INTERVAL '30' SECOND) ORDER BY created_at limit :limit for update skip locked";

        return namedParameterJdbcTemplate.query(query, Map.of("limit", limit),
                new BeanPropertyRowMapper<>(Outbox.class));
    }
    
}
