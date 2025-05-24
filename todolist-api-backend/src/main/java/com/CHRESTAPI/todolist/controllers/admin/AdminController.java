package com.CHRESTAPI.todolist.controllers.admin;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return "Admin dashboard - accessible only to admins";
    }

    @GetMapping("/users")
    public String getUsers() {
        return "User management - accessible only to admins";
    }
}