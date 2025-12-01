package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Customer;
import com.springboot.dev_spring_boot_demo.dao.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public long countAllCustomers() {
        return customerRepository.count();
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean emailExists(String email) {
        return customerRepository.existsByEmail(email);
    }
}
