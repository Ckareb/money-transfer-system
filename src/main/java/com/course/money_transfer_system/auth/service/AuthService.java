package com.course.money_transfer_system.auth.service;

import com.course.money_transfer_system.auth.config.JwtUtil;
import com.course.money_transfer_system.auth.model.UserAccount;
import com.course.money_transfer_system.exception.IncorrectParamException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Service
public class AuthService {

    private final UserAccountService userAccountService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserAccountService userAccountService,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userAccountService = userAccountService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (!authHeader.startsWith("Basic ")) {
            throw new SecurityException("Ошибка в запросе при авторизации");
        }

        // Извлекаем Base64-закодированные credentials
        String base64Credentials = authHeader.substring(6); // Убираем "Basic "
        String credentials;
        try {
            credentials = new String(Base64.getDecoder().decode(base64Credentials));
        } catch (IllegalArgumentException e) {
            throw new SecurityException("Ошибка в запросе при авторизации");
        }

        // Разделяем username:password
        String[] parts = credentials.split(":", 2);

        String username = parts[0];
        String password = parts[1];

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IncorrectParamException("Пустой логин или пароль");
        }

        UserAccount user = userAccountService.findByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectParamException("Не верный пароль");
        }

        String role = roleService.getUserRole(user.getUsername());
        return jwtUtil.generateToken(username, role);
    }

    public static String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private static boolean authorityRole(String authorityUserRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase(authorityUserRole)) {
                return true;
            }
        }

        return false;
    }


    public static boolean equalsRoles(String role) {
        return authorityRole(role);
    }
}
