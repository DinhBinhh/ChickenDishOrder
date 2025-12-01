package com.springboot.dev_spring_boot_demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {
    // Các role constants
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_USER = "USER";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                if (rawPassword.toString().startsWith("{bcrypt}")) {
                    return rawPassword.toString();
                }
                return "{bcrypt}" + bcrypt.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                String storedPassword = encodedPassword.replaceFirst("^\\{bcrypt\\}", "");
                return bcrypt.matches(rawPassword, storedPassword);
            }
        };
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource) {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                List<GrantedAuthority> authorities = getJdbcTemplate().query(
                        "SELECT authority FROM authorities WHERE username = ?",
                        (rs, rowNum) -> new SimpleGrantedAuthority(rs.getString(1)),
                        username);

                // Kiểm tra nếu chỉ có 1 role và không phải USER thì không cho đăng nhập
                if (authorities.size() == 1 &&
                        !authorities.get(0).getAuthority().equals("ROLE_" + ROLE_USER)) {
                    throw new UsernameNotFoundException("User has invalid role configuration");
                }

                List<UserDetails> users = getJdbcTemplate().query(
                        "SELECT username, password, enabled FROM users WHERE username = ?",
                        (rs, rowNum) -> User.builder()
                                .username(rs.getString(1))
                                .password(rs.getString(2))
                                .disabled(!rs.getBoolean(3))
                                .authorities(authorities.stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toArray(String[]::new))
                                .build(),
                        username);

                if (users.isEmpty()) {
                    throw new UsernameNotFoundException("User not found");
                }

                return users.get(0);
            }
        };

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, enabled FROM users WHERE username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT username, authority FROM authorities WHERE username=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                // Public pages
                                .requestMatchers(
                                        "/",
                                        "/home/**",
                                        "/menu",
                                        "/promotions",
                                        "/about",
                                        "/contact",
                                        "/login",
                                        "/register",
                                        "/css/**",
                                        "/js/**",
                                        "/default/images/**"
                                ).permitAll()

                                // Admin pages require all 3 roles
                                .requestMatchers("/admin/**").access(new WebExpressionAuthorizationManager(
                                        "hasRole('ADMIN') and hasRole('MANAGER') and hasRole('USER')"))

                                // System pages require both MANAGER and USER roles
                                .requestMatchers("/system/**").access(new WebExpressionAuthorizationManager(
                                        "hasRole('MANAGER') and hasRole('USER')"))

                                // All other requests require authentication
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/?logout")
                                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}