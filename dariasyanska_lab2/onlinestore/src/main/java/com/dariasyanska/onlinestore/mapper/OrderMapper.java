package com.dariasyanska.onlinestore.mapper;

import com.dariasyanska.onlinestore.dto.OrderDto;
import com.dariasyanska.onlinestore.model.Order;
import com.dariasyanska.onlinestore.model.Product;
import com.dariasyanska.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

  @Autowired
  private ProductRepository productRepository;

  public OrderDto toDto(Order order) {
    OrderDto dto = new OrderDto();
    dto.setId(order.getId());
    dto.setCustomerName(order.getCustomerName());
    dto.setOrderDate(order.getOrderDate());
    dto.setTotalPrice(order.getTotalPrice());
    dto.setProductNames(order.getProducts()
        .stream()
        .map(Product::getName)
        .collect(Collectors.toList()));
    return dto;
  }

  public Order toEntity(OrderDto dto) {
    Order order = new Order();
    order.setId(dto.getId());
    order.setCustomerName(dto.getCustomerName());
    order.setOrderDate(dto.getOrderDate());
    order.setTotalPrice(dto.getTotalPrice());
    List<Product> products = dto.getProductNames()
        .stream()
        .map(name -> productRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Product not found: " + name)))
        .collect(Collectors.toList());
    order.setProducts(products);
    return order;
  }
}