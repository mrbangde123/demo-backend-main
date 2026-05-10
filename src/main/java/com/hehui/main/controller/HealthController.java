package com.hehui.main.controller;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Value("${app.version}")
    private String version;

    @Value("${app.env-name}")
    private String envName;

    private final Environment environment;

    public HealthController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "demo-backend-main");
        result.put("version", version);
        result.put("envName", envName);
        result.put("activeProfiles", environment.getActiveProfiles());
        result.put("timestamp", LocalDateTime.now().toString());
        return result;
    }
}