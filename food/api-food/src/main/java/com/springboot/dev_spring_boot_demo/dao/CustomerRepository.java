package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}