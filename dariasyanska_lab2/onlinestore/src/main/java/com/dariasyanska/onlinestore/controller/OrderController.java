package com.dariasyanska.onlinestore.controller;

import com.dariasyanska.onlinestore.dto.OrderDto;
import com.dariasyanska.onlinestore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  @Operation(summary = "Create new order")
  public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto dto) {
    return ResponseEntity.ok(orderService.createOrder(dto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get order by ID")
  public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @GetMapping
  @Operation(summary = "Get all orders")
  public ResponseEntity<List<OrderDto>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update order by ID")
  public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody OrderDto dto) {
    return ResponseEntity.ok(orderService.updateOrder(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete order by ID")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}