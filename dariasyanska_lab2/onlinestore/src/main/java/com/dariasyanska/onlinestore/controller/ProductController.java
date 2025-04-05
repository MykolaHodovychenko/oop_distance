package com.dariasyanska.onlinestore.controller;

import com.dariasyanska.onlinestore.dto.ProductDto;
import com.dariasyanska.onlinestore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  @Operation(summary = "Create new product")
  public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) {
    return ResponseEntity.ok(productService.createProduct(dto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get product by ID")
  public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @GetMapping
  @Operation(summary = "Get all products")
  public ResponseEntity<List<ProductDto>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update product by ID")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
    return ResponseEntity.ok(productService.updateProduct(id, dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete product by ID")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}