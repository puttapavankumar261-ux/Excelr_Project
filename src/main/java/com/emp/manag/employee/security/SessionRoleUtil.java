package com.emp.manag.employee.security;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionRoleUtil {

    public static void requireAdmin(HttpSession session) {

        String role =
                (String) session.getAttribute("role");

        if (role == null ||
                !role.equalsIgnoreCase("ADMIN")) {

            throw new RuntimeException(
                    "Access denied. ADMIN only.");
        }
    }

    public static void requireHrOrAdmin(
            HttpSession session) {

        String role =
                (String) session.getAttribute("role");

        if (role == null ||
            (!role.equalsIgnoreCase("ADMIN")
             && !role.equalsIgnoreCase("HR"))) {

            throw new RuntimeException(
                    "Access denied.");
        }
    }
}