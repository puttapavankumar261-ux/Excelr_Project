package com.emp.manag.setup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.emp.manag.setup.dto.AdminSetupRequest;
import com.emp.manag.setup.service.SetupService;

@RestController
@RequestMapping("/api/employee-management/setup")
public class SetupController {

    @Autowired
    private SetupService setupService;

    @PostMapping("/admin")
    public String createAdmin(
            @RequestBody AdminSetupRequest request) {

        return setupService.createAdmin(request);
    }

    @GetMapping("/initialized")
    public boolean isInitialized() {

        return setupService.isInitialized();
    }
}