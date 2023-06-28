package com.demo.productservice.mapping;

import com.demo.productservice.domain.ProductEntity;
import com.demo.productservice.model.Product;
import com.demo.productservice.model.ProductRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity requestToEntity(ProductRequest productRequest);

    Product entityToDto(ProductEntity product);
}
