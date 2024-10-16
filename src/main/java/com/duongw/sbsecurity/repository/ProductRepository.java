package com.duongw.sbsecurity.repository;

import com.duongw.sbsecurity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> findByNameContaining(String productName);
}
