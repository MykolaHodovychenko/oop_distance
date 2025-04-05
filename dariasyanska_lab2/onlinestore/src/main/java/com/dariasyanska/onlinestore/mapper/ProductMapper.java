package com.dariasyanska.onlinestore.mapper;

import com.dariasyanska.onlinestore.dto.ProductDto;
import com.dariasyanska.onlinestore.model.Product;

public class ProductMapper {

  public static ProductDto toDto(Product product) {
    return new ProductDto(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice()
    );
  }

  public static Product toEntity(ProductDto dto) {
    Product product = new Product();
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    return product;
  }
}