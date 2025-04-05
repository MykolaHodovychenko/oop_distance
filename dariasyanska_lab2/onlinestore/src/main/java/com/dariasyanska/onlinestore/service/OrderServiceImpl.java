package com.dariasyanska.onlinestore.service;

import com.dariasyanska.onlinestore.dto.OrderDto;
import com.dariasyanska.onlinestore.mapper.OrderMapper;
import com.dariasyanska.onlinestore.model.Order;
import com.dariasyanska.onlinestore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  @Override
  public OrderDto createOrder(OrderDto dto) {
    Order order = orderMapper.toEntity(dto);
    return orderMapper.toDto(orderRepository.save(order));
  }

  @Override
  public OrderDto getOrderById(Long id) {
    return orderRepository.findById(id)
        .map(orderMapper::toDto)
        .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
  }

  @Override
  public List<OrderDto> getAllOrders() {
    return orderRepository.findAll()
        .stream()
        .map(orderMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public OrderDto updateOrder(Long id, OrderDto dto) {
    Order existing = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    existing.setCustomerName(dto.getCustomerName());
    existing.setOrderDate(dto.getOrderDate());
    existing.setTotalPrice(dto.getTotalPrice());
    existing.setProducts(orderMapper.toEntity(dto).getProducts());
    return orderMapper.toDto(orderRepository.save(existing));
  }

  @Override
  public void deleteOrder(Long id) {
    orderRepository.deleteById(id);
  }
}