package com.dariasyanska.onlinestore.repository;

import com.dariasyanska.onlinestore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}