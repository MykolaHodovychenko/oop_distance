package com.dariasyanska.onlinestore.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderDto {
  private Long id;
  private String customerName;
  private LocalDate orderDate;
  private Double totalPrice;
  private List<String> productNames;

  // Getters & Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getCustomerName() { return customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }
  public LocalDate getOrderDate() { return orderDate; }
  public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
  public Double getTotalPrice() { return totalPrice; }
  public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
  public List<String> getProductNames() { return productNames; }
  public void setProductNames(List<String> productNames) { this.productNames = productNames; }
}