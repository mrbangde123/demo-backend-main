package com.hehui.main.controller;

  import com.hehui.main.entity.User;
  import com.hehui.main.service.UserService;
  import java.util.HashMap;
  import java.util.Map;

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


      public UserController(UserService userService, RestTemplate restTemplate) {
          this.userService = userService;
          this.restTemplate = restTemplate;
      }

      @GetMapping("/{id}")
      public User getUser(@PathVariable Integer id) {
          return userService.getById(id);
      }

    // 根据用户 ID 查出名字，再去 client 服务拿问候语
    @GetMapping("/{id}/greet")
    public Map<String, Object> greetUser(@PathVariable Integer id) {
        // 第 1 步：从 MySQL 查用户
        User user = userService.getById(id);
        if (user == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "User not found");
            return error;
        }

        // 第 2 步：用 RestTemplate 调用 client 服务
        String clientUrl = "http://localhost:8081/api/client/greet/" + user.getName();
        Map clientResult = restTemplate.getForObject(clientUrl, Map.class);

        // 第 3 步：组装结果返回
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("userName", user.getName());
        result.put("greeting", clientResult.get("greeting"));
        result.put("from", clientResult.get("from"));
        return result;
    }
  }