package com.dariasyanska.onlinestore.service;

import com.dariasyanska.onlinestore.dto.OrderDto;

import java.util.List;

public interface OrderService {
  OrderDto createOrder(OrderDto dto);
  OrderDto getOrderById(Long id);
  List<OrderDto> getAllOrders();
  OrderDto updateOrder(Long id, OrderDto dto);
  void deleteOrder(Long id);
}