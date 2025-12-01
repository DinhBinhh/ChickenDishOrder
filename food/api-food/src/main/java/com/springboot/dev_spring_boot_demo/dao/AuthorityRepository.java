package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    List<Authority> findByUsername(String username);
    List<Authority> findByAuthority(String authority);

    @Transactional
    @Modifying
    @Query("DELETE FROM Authority a WHERE a.username = :username")
    void deleteByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Authority a WHERE a.username = :username AND a.authority = :authority")
    void deleteByUsernameAndAuthority(String username, String authority);
    boolean existsByUsernameAndAuthority(String username, String authority);
}
