package com.dariasyanska.onlinestore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @EmbeddedId
  private OrderItemId id = new OrderItemId();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  private Product product;

  @Column(nullable = false)
  private int quantity;

  public OrderItem() {}

  public OrderItem(Order order, Product product, int quantity) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.id = new OrderItemId(order.getId(), product.getId());
  }

  // Getters and setters

  public Order getOrder() { return order; }

  public void setOrder(Order order) { this.order = order; }

  public Product getProduct() { return product; }

  public void setProduct(Product product) { this.product = product; }

  public int getQuantity() { return quantity; }

  public void setQuantity(int quantity) { this.quantity = quantity; }

  public OrderItemId getId() { return id; }

  public void setId(OrderItemId id) { this.id = id; }
}