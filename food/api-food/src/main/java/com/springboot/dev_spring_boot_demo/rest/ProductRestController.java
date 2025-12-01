package com.springboot.dev_spring_boot_demo.rest;

import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

// Custom Response Structure
class ApiResponse<T> {
    private String message;
    private T data;
    private Long totalItems;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message, T data, Long totalItems) {
        this.message = message;
        this.data = data;
        this.totalItems = totalItems;
    }

    // Getters
    public String getMessage() { return message; }
    public T getData() { return data; }
    public Long getTotalItems() { return totalItems; }
}

// Custom Exceptions
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}

class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}

// Main REST Controller
@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Product.Category category,
            @RequestParam(required = false) Integer maxSpicy,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) String search) {

        List<Product> allProducts;

        // Lấy toàn bộ danh sách sản phẩm
        if (category != null) {
            allProducts = productService.getProductsByCategory(category);
        } else if (featured != null && featured) {
            allProducts = productService.getFeaturedProducts();
        } else {
            allProducts = productService.getAllProducts(); // Sử dụng phương thức gốc
        }

        // Thực hiện filter
        if (search != null && !search.isEmpty()) {
            allProducts = allProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        if (maxSpicy != null) {
            allProducts = allProducts.stream()
                    .filter(p -> p.getSpicyLevel() <= maxSpicy)
                    .toList();
        }

        if (minPrice != null) {
            allProducts = allProducts.stream()
                    .filter(p -> p.getPrice().compareTo(minPrice) >= 0)
                    .toList();
        }

        if (maxPrice != null) {
            allProducts = allProducts.stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .toList();
        }

        // Thực hiện phân trang thủ công
        int start = page * size;
        int end = Math.min(start + size, allProducts.size());
        List<Product> pagedProducts = allProducts.subList(start, end);

        return ResponseEntity.ok(
                new ApiResponse<>("Success", pagedProducts, (long) allProducts.size())
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return ResponseEntity.ok(new ApiResponse<>("Success", product));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Product.Category>>> getAllCategories() {
        return ResponseEntity.ok(
                new ApiResponse<>("Success", List.of(Product.Category.values()),
                        productService.countAllCategories())
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Product>> addProduct(@Valid @RequestBody Product product) {
        product.setId(null); // Ensure it's a new product
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created successfully", savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody Product product) {

        if (!id.equals(product.getId())) {
            throw new BadRequestException("ID in path and body do not match");
        }

        if (productService.getProductById(id) == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        Product updatedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Integer id) {
        if (productService.getProductById(id) == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<ProductStats>> getProductStats() {
        long totalProducts = productService.countAllProducts();
        long totalCategories = productService.countAllCategories();
        long featuredProducts = productService.getFeaturedProducts().size();

        ProductStats stats = new ProductStats(totalProducts, totalCategories, featuredProducts);
        return ResponseEntity.ok(new ApiResponse<>("Product statistics", stats));
    }

    // Statistics DTO
    private static class ProductStats {
        private final long totalProducts;
        private final long totalCategories;
        private final long featuredProducts;

        public ProductStats(long totalProducts, long totalCategories, long featuredProducts) {
            this.totalProducts = totalProducts;
            this.totalCategories = totalCategories;
            this.featuredProducts = featuredProducts;
        }

        // Getters
        public long getTotalProducts() { return totalProducts; }
        public long getTotalCategories() { return totalCategories; }
        public long getFeaturedProducts() { return featuredProducts; }
    }
}