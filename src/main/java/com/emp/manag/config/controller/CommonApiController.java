package com.emp.manag.config.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.manag.config.ApiConfig;

@RestController
@RequestMapping("/employee-management")
public class CommonApiController {

	private final ApiConfig apiConfig;

    // Constructor injection is the recommended best practice
    public CommonApiController(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @GetMapping("/info")
    public Map<String, Object> getApiInformation() {
        Map<String, Object> response = new HashMap<>();
        response.put("systemTitle", apiConfig.getTitle());
        response.put("systemVersion", apiConfig.getVersion());
        response.put("allowedConnections", apiConfig.getMaxConnections());
        response.put("activeModules", apiConfig.getEnabledFeatures());
        return response;
    }
	
}
