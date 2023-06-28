package com.demo.productservice.integrationtests.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@TestConfiguration
@EnableKafka
public class TestConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String kafka;

    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        final var consumerProps = KafkaTestUtils.consumerProps(kafka, "test", "false");
        consumerProps.put("auto.offset.reset", "earliest");
        consumerProps.put("key.serializer", StringSerializer.class);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new KafkaConsumer<>(consumerProps);
    }
}
