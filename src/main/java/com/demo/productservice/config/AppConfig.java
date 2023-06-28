package com.demo.productservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@OpenAPIDefinition
@EnableScheduling
@EnableAsync
public class AppConfig {


}
