package com.hehui.main.controller;

  import com.hehui.main.entity.User;
  import com.hehui.main.service.UserService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;

  @RestController
  @RequestMapping("/api/users")
  public class UserController {


      private final UserService userService;

      public UserController(UserService userService) {
          this.userService = userService;
      }

      @GetMapping("/{id}")
      public User getUser(@PathVariable Integer id) {
          return userService.getById(id);
      }
  }