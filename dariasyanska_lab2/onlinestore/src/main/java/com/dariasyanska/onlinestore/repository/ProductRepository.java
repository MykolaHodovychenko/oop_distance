package com.dariasyanska.onlinestore.repository;

import com.dariasyanska.onlinestore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findByName(String name);
}