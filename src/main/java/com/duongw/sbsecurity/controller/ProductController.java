package com.duongw.sbsecurity.controller;

import com.duongw.sbsecurity.DTO.response.ApiResponse;
import com.duongw.sbsecurity.entity.Product;
import com.duongw.sbsecurity.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/products")
public class ProductController {

    private final IProductService productService;

    // ADMIN và STAFF có thể tạo mới sản phẩm
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_STAFF')")
    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse<?>> createProduct(@RequestBody Product product) {
        log.info("---------- createProduct ----------");
        try {
            Product createdProduct = productService.createProduct(product);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED, "Product created successfully", createdProduct), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Product creation failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    // ADMIN và STAFF có thể cập nhật sản phẩm
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_STAFF')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable(name = "id") Long id, @RequestBody Product product) {
        log.info("---------- updateProduct ----------");
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Product updated successfully", updatedProduct), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Product update failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    // Chỉ ADMIN có quyền xóa sản phẩm
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable(name = "id") Long id) {
        log.info("---------- deleteProduct ----------");
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Product deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Product deletion failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    // CUSTOMER có thể xem thông tin chi tiết sản phẩm
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable(name = "id") Long id) {
        log.info("---------- getProductById ----------");
        try {
            Product product = productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Get product by id success", product), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Get product by id failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    // CUSTOMER có thể xem tất cả các sản phẩm
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping(path = "/all")
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        log.info("---------- getAllProducts ----------");
        try {
            List<Product> productList = productService.getAllProducts();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Get all products success", productList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Get all products failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    // Tìm kiếm sản phẩm theo tên (không phân biệt vai trò)
    @GetMapping(path = "/search")
    public ResponseEntity<ApiResponse<?>> searchProductsByName(@RequestParam(name = "name") String name) {
        log.info("---------- searchProductsByName ----------");
        try {
            List<Product> products = productService.searchProductsByName(name);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Search products by name success", products), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Search products by name failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    // Tìm kiếm sản phẩm theo khoảng giá
    @GetMapping(path = "/search-by-price")
    public ResponseEntity<ApiResponse<?>> searchProductsByPriceRange(@RequestParam(name = "minPrice") Double minPrice, @RequestParam(name = "maxPrice") Double maxPrice) {
        log.info("---------- searchProductsByPriceRange ----------");
        try {
            List<Product> products = productService.findByPriceBetween(minPrice, maxPrice);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Search products by price range success", products), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Search products by price range failed", null), HttpStatus.BAD_REQUEST);
        }
    }
}
