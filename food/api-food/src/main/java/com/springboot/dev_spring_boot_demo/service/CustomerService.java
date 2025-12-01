package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    Customer saveCustomer(Customer customer);
    void deleteCustomer(Long id);
    boolean emailExists(String email);
    long countAllCustomers();
}