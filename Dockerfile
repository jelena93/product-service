FROM amazoncorretto:17.0.3-alpine

COPY target/product-service.jar product-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "product-service.jar"]