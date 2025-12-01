package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Product.Category category);
    List<Product> getFeaturedProducts();
    Product getProductById(Integer id);
    Product saveProduct(Product product);
    void deleteProduct(Integer id);
    long countAllProducts();
    long countAllCategories();
}