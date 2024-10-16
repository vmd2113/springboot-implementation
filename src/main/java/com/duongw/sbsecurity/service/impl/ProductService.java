package com.duongw.sbsecurity.service.impl;

import com.duongw.sbsecurity.entity.Product;
import com.duongw.sbsecurity.repository.ProductRepository;
import com.duongw.sbsecurity.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    @Override
    public Product createProduct(Product product) {
        Product product1  = new Product();
        product1.setDescription(product.getDescription());
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        return productRepository.save(product1);
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Product product1 = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product1.setDescription(product.getDescription());
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        return productRepository.save(product1);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);

    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        return Optional.of(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getProductsByPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> searchProductsByName(String productName) {
        return productRepository.findByNameContaining(productName);
    }

    @Override
    public List<Product> findByPriceBetween(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
