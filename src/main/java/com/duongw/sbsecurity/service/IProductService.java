package com.duongw.sbsecurity.service;

import com.duongw.sbsecurity.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    // Tạo mới sản phẩm
    Product createProduct(Product productDTO);

    // Cập nhật sản phẩm
    Product updateProduct(Long productId, Product productDTO);

    // Xóa sản phẩm
    void deleteProduct(Long productId);

    // Lấy sản phẩm theo ID
    Optional<Product> getProductById(Long productId);

    // Lấy danh sách tất cả các sản phẩm
    List<Product> getAllProducts();

    // Lấy danh sách sản phẩm phân trang
    Page<Product> getProductsByPage(Pageable pageable);

    // Tìm kiếm sản phẩm theo tên
    List<Product> searchProductsByName(String productName);

    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
}
