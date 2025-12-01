package com.springboot.dev_spring_boot_demo.controller;

import com.springboot.dev_spring_boot_demo.entity.Authority;
import com.springboot.dev_spring_boot_demo.entity.User;
import com.springboot.dev_spring_boot_demo.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String systemPage(Model model) {
        List<User> users = systemService.getAllUsers();
        List<Authority> authorities = systemService.getAllAuthorities();

        // Nhóm roles theo username (key: username, value: danh sách roles)
        Map<String, List<String>> userRolesMap = authorities.stream()
                .collect(Collectors.groupingBy(
                        Authority::getUsername,
                        Collectors.mapping(Authority::getAuthority, Collectors.toList()))
                );

        model.addAttribute("users", users);
        model.addAttribute("authorities", authorities); // THÊM DÒNG NÀY
        model.addAttribute("userRolesMap", userRolesMap);
        return "system";
    }


    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user,
                          @RequestParam(value = "roles", required = false) String[] roles,
                          RedirectAttributes redirectAttributes) {
        try {
            // Mã hóa password nếu chưa mã hóa
            if (!user.getPassword().startsWith("{bcrypt}")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Lưu user trước
            user.setEnabled(true);
            systemService.saveUser(user);

            // Thêm ROLE_USER mặc định
            systemService.saveAuthority(new Authority(user.getUsername(), "ROLE_USER"));

            // Thêm các role khác nếu có
            if (roles != null) {
                for (String role : roles) {
                    if (!role.equals("ROLE_USER")) { // Tránh thêm trùng ROLE_USER
                        systemService.saveAuthority(new Authority(user.getUsername(), role));
                    }
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Thêm user thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/system";
    }
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(value = "roles", required = false) String[] roles,
                             @RequestParam(value = "newPassword", required = false) String newPassword,
                             RedirectAttributes redirectAttributes) {
        try {
            // Nếu có mật khẩu mới thì mã hóa và cập nhật
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            }

            // Kiểm tra quyền nếu có role ADMIN
            if (!isAdmin() && roles != null && Arrays.asList(roles).contains("ROLE_ADMIN")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền gán role ADMIN");
                return "redirect:/system";
            }

            // Cập nhật user
            systemService.updateUser(user);

            // Cập nhật roles nếu có
            if (roles != null) {
                // Xóa hết roles cũ
                systemService.deleteAuthoritiesByUsername(user.getUsername());

                // Thêm roles mới
                for (String role : roles) {
                    systemService.saveAuthority(new Authority(user.getUsername(), role));
                }
            }

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật user thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật user: " + e.getMessage());
        }
        return "redirect:/system";
    }
    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam String username,
                             RedirectAttributes redirectAttributes) {
        try {
            // Xóa các role trước (bảng con)
            systemService.deleteAuthoritiesByUsername(username);

            // Sau đó mới xóa user (bảng cha)
            systemService.deleteUser(username);

            redirectAttributes.addFlashAttribute("successMessage", "Xóa user thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Lỗi khi xóa user: " + e.getMessage());
        }
        return "redirect:/system";
    }

    @PostMapping("/roles/add")
    public String addRole(@RequestParam String username,
                          @RequestParam String role,
                          RedirectAttributes redirectAttributes) {
        systemService.saveAuthority(new Authority(username, role));
        redirectAttributes.addFlashAttribute("successMessage", "Role assigned successfully");
        return "redirect:/system";
    }

    @GetMapping("/roles/delete")
    public String deleteRole(@RequestParam String username,
                             @RequestParam String role,
                             RedirectAttributes redirectAttributes) {
        systemService.deleteAuthority(username, role);
        redirectAttributes.addFlashAttribute("successMessage", "Role removed successfully");
        return "redirect:/system";
    }
}
