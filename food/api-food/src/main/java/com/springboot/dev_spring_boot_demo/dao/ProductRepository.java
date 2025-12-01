package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(Product.Category category);
    List<Product> findByIsFeaturedTrue();
    List<Product> findBySpicyLevelLessThanEqual(int maxSpicy);
    @Query("SELECT COUNT(DISTINCT p.category) FROM Product p")
    long countDistinctCategories();
}