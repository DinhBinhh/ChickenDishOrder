package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Hiển thị danh sách sản phẩm khi truy cập /menu
    @GetMapping("/menu")
    public String showMenu(Model model) {

        model.addAttribute("products", productService.getAllProducts());
        return "menu";
    }

//    // Các endpoint khác (giữ nguyên nếu cần)
//    @GetMapping("/products/category/{category}")
//    public String productsByCategory(@PathVariable Product.Category category, Model model) {
//        model.addAttribute("products", productService.getProductsByCategory(category));
//        model.addAttribute("categoryName", category.name());
//        return "products/category";
//    }
//
//    @GetMapping("/products/featured")
//    public String featuredProducts(Model model) {
//        model.addAttribute("products", productService.getFeaturedProducts());
//        return "products/featured";
//    }
}