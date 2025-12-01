package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.entity.User;
import java.util.List;

public interface SystemService {
    List<User> getAllUsers();
    List<Authority> getAllAuthorities();
    User saveUser(User user);
    void deleteUser(String username);
    Authority saveAuthority(Authority authority);
    void deleteAuthority(String username, String authority);
    User updateUser(User user);
    void deleteAuthoritiesByUsername(String username);
}
