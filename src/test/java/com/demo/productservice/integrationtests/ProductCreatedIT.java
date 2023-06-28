package com.demo.productservice.integrationtests;

import com.demo.productservice.integrationtests.config.TestConfig;
import com.demo.productservice.model.Product;
import com.demo.productservice.repository.ProductRepository;
import com.demo.productservice.util.ProductTestUtil;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestConfig.class)
public class ProductCreatedIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaConsumer<String, String> kafkaConsumer;
    private final String topic = "product";

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        kafkaConsumer.subscribe(Collections.singleton(topic));
    }

    @AfterEach
    public void tearDown() {
        kafkaConsumer.close();
    }

    @Test
    public void testCreateProduct() {
        final var productName = "Test Product";
        final var response = restTemplate.postForEntity("http://localhost:" + port + "/product",
                ProductTestUtil.createProductRequest(productName, new BigDecimal("20.30")), Product.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var savedProduct = productRepository.findByName(productName);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal("20.30"));

        final var consumerRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, topic);

        final var product = consumerRecord.value();
        assertThat(product).isNotNull();

    }
}
