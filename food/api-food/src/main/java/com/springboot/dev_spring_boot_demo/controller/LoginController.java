package com.springboot.dev_spring_boot_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Trang chính
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Các trang authentication
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }


    @GetMapping("/promotions")
    public String promotions() {
        return "promotions";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }





    // Trang lỗi
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}