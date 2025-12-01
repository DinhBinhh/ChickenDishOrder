package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.dao.ProductRepository;
import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Product.Category category) {
        return productRepository.findByCategory(category);
    }
    @Override
    public long countAllProducts() {
        return productRepository.count();
    }

    @Override
    public long countAllCategories() {
        // Đếm số lượng category riêng biệt
        return productRepository.countDistinctCategories();
    }

    @Override
    public List<Product> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue();
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}