package com.hehui.main.controller;

import com.hehui.main.entity.User;
import com.hehui.main.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RestTemplate restTemplate;
    private final String clientUrl;
    private final String envName;



    public UserController(UserService userService, RestTemplate restTemplate,
                          @Value("${app.client-url}") String clientUrl,
                          @Value("${app.env-name}") String envName) {
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.clientUrl = clientUrl;
        this.envName = envName;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping("/{id}/greet")
    public Map<String, Object> greetUser(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "User not found");
            return error;
        }

        String url = clientUrl + "/api/client/greet/" + user.getName();
        Map clientResult = restTemplate.getForObject(url, Map.class);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("userName", user.getName());
        result.put("greeting", clientResult.get("greeting"));
        result.put("from", clientResult.get("from"));
        return result;
    }


    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from demo-backend-main!", "env", envName);
    }


}