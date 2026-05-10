package com.hehui.main.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Value("${app.version}")
    private String version;

    @Value("${app.env-name}")
    private String envname;

    @GetMapping
    private String aaa() {
        return version + "   " + envname;
    }
}
