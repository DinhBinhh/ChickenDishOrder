package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

