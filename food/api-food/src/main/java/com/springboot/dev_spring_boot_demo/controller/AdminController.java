package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Customer;
import com.springboot.dev_spring_boot_demo.entity.Product;
import com.springboot.dev_spring_boot_demo.service.CustomerService;
import com.springboot.dev_spring_boot_demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping
    public String adminDashboard(@RequestParam(name = "tab", defaultValue = "dashboard") String tab, Model model) {
        // Products
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("newProduct", new Product());
        model.addAttribute("editProduct", new Product());
        model.addAttribute("categories", Product.Category.values());

        // Customers
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("newCustomer", new Customer());
        model.addAttribute("editCustomer", new Customer());

        // Dashboard stats
        model.addAttribute("customerCount", customerService.countAllCustomers());
        model.addAttribute("productCount", productService.countAllProducts());
        model.addAttribute("categoryCount", productService.countAllCategories());

        // Set active tab
        model.addAttribute("activeTab", tab);

        return "admin";
    }
    // Thêm các endpoint riêng nhưng vẫn trả về cùng template
    @GetMapping("/products")
    public String showProducts(Model model) {
        // Chuyển hướng với tham số tab
        return "redirect:/admin?tab=products";
    }

    @GetMapping("/customers")
    public String showCustomers(Model model) {
        // Chuyển hướng với tham số tab
        return "redirect:/admin?tab=customers";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Chuyển hướng với tham số tab
        return "redirect:/admin?tab=dashboard";
    }


    // ========== PRODUCT METHODS ==========

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute("newProduct") Product product,
                              BindingResult result,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {

        // Thêm kiểm tra riêng cho category
        if (product.getCategory() == null) {
            result.rejectValue("category", "error.product", "Vui lòng chọn danh mục");
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newProduct", result);
            redirectAttributes.addFlashAttribute("newProduct", product);
            redirectAttributes.addFlashAttribute("categories", Product.Category.values()); // Đảm bảo categories được truyền lại
            return "redirect:/admin";
        }

        try {
            if (!imageFile.isEmpty()) {
                String fileName = storeImage(imageFile);
                product.setImage(fileName);
            }

            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu sản phẩm: " + e.getMessage());
            redirectAttributes.addFlashAttribute("newProduct", product);
        }

        return "redirect:/admin";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            redirectAttributes.addFlashAttribute("editProduct", product);
            redirectAttributes.addFlashAttribute("categories", Product.Category.values());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/products/update")
    public String updateProduct(@Valid @ModelAttribute("editProduct") Product product,
                                BindingResult result,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editProduct", result);
            redirectAttributes.addFlashAttribute("editProduct", product);
            return "redirect:/admin";
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                // Xóa ảnh cũ nếu có
                if (product.getImage() != null) {
                    deleteImage(product.getImage());
                }
                String fileName = storeImage(imageFile);
                product.setImage(fileName);
            }

            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            redirectAttributes.addFlashAttribute("editProduct", product);
        }

        return "redirect:/admin";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            if (product.getImage() != null) {
                deleteImage(product.getImage());
            }
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Sản phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    // ========== CUSTOMER METHODS ==========
    @PostMapping("/customers/save")
    public String saveCustomer(@Valid @ModelAttribute("newCustomer") Customer customer,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newCustomer", result);
            redirectAttributes.addFlashAttribute("newCustomer", customer);
            return "redirect:/admin";
        }

        try {
            customerService.saveCustomer(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được lưu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu khách hàng: " + e.getMessage());
            redirectAttributes.addFlashAttribute("newCustomer", customer);
        }

        return "redirect:/admin";
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomerForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.getCustomerById(id);
            redirectAttributes.addFlashAttribute("editCustomer", customer);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khách hàng: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/customers/update")
    public String updateCustomer(@Valid @ModelAttribute("editCustomer") Customer customer,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editCustomer", result);
            redirectAttributes.addFlashAttribute("editCustomer", customer);
            return "redirect:/admin";
        }

        try {
            customerService.saveCustomer(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật khách hàng: " + e.getMessage());
            redirectAttributes.addFlashAttribute("editCustomer", customer);
        }

        return "redirect:/admin";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Khách hàng đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa khách hàng: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    // ========== PRIVATE METHODS ==========
    private String storeImage(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        try (InputStream inputStream = imageFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    private void deleteImage(String imageName) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(imageName);
        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
        }
    }
}