package com.springboot.dev_spring_boot_demo.service;

import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.dao.AuthorityRepository;
import com.springboot.dev_spring_boot_demo.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public void deleteAuthoritiesByUsername(String username) {
        authorityRepository.deleteByUsername(username);
    }

    @Override
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        // Delete authorities first
        authorityRepository.deleteByUsername(username);
        // Then delete user
        userRepository.deleteById(username);
    }

    @Override
    @Transactional
    public Authority saveAuthority(Authority authority) {
        // Kiểm tra xem role đã tồn tại chưa
        if (!authorityRepository.existsByUsernameAndAuthority(
                authority.getUsername(),
                authority.getAuthority())) {
            return authorityRepository.save(authority);
        }
        return authority; // Nếu đã tồn tại thì không lưu lại
    }

    @Override
    public void deleteAuthority(String username, String authority) {
        authorityRepository.deleteByUsernameAndAuthority(username, authority);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

}
