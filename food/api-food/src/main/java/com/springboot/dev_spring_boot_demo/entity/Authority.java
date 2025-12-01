package com.springboot.dev_spring_boot_demo.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    private String username;
    private String authority;

    // Thêm constructor mới
    public Authority() {} // Constructor mặc định bắt buộc cho JPA

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    // getters và setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}