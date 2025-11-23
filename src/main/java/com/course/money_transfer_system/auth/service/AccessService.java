package com.course.money_transfer_system.auth.service;

public class AccessService {

    public static boolean isAdmin() {
        return AuthService.equalsRoles("ADMIN");
    }

    public static boolean isUser() {
        return AuthService.equalsRoles("USER");
    }
}
