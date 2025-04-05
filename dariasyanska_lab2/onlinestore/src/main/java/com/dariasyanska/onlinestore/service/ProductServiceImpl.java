package com.dariasyanska.onlinestore.service;

import com.dariasyanska.onlinestore.dto.ProductDto;
import com.dariasyanska.onlinestore.exception.NotFoundException;
import com.dariasyanska.onlinestore.mapper.ProductMapper;
import com.dariasyanska.onlinestore.model.Product;
import com.dariasyanska.onlinestore.repository.ProductRepository;
import com.dariasyanska.onlinestore.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public ProductDto createProduct(ProductDto dto) {
    Product product = ProductMapper.toEntity(dto);
    return ProductMapper.toDto(productRepository.save(product));
  }

  @Override
  public ProductDto getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found"));
    return ProductMapper.toDto(product);
  }

  @Override
  public List<ProductDto> getAllProducts() {
    return productRepository.findAll()
        .stream()
        .map(ProductMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ProductDto updateProduct(Long id, ProductDto dto) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Product not found"));
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    return ProductMapper.toDto(productRepository.save(product));
  }

  @Override
  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}