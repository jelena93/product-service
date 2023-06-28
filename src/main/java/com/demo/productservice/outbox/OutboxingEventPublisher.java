package com.demo.productservice.outbox;

import com.demo.productservice.outbox.model.OutboxEvent;
import com.demo.productservice.repository.OutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OutboxingEventPublisher {
    private static final int MAX_LIMIT = 100;

    private OutboxRepository repository;
    private TransactionTemplate transactionTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOutboxEvent(final OutboxEvent event) {
        log.info("Received published Outbox Event {} ", event);
        sendMessage(event);
    }

    @Scheduled(fixedDelayString = "${app.outbox.scheduled.delayString:PT30S}",
            initialDelayString = "${app.outbox.scheduled.initialDelayString:PT30S}")
    public void publishEvents() {
        sendEvents(MAX_LIMIT);
    }

    public void sendEvents(int limit) {
        Integer noSentEvents;
        do {
            noSentEvents = transactionTemplate.execute(status -> {
                final var outboxedMessages = repository.fetchUnsent(limit);
                final var sentEvents = outboxedMessages.stream()
                        .map(message -> {
                            try {
                                log.info("Sending Outbox message {} ", message);
                                sendMessage(message);
                                return message;
                            } catch (Exception e) {
                                log.error("Failed to send message from outbox", e);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                repository.deleteByIds(sentEvents);
                return outboxedMessages.size();
            });
        } while (noSentEvents != null && noSentEvents >= limit);
    }

    public void sendMessage(final OutboxEvent event) {
        kafkaTemplate.send(event.getAggregateType(), event.getPayload())
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent message [{}]", event);
                        repository.deleteById(event.getId());
                    } else {
                        log.error("Unable to send message=[{}] due to {}", event, ex.getMessage());
                    }
                });
    }

}
