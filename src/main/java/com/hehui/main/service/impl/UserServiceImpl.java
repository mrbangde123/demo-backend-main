package com.hehui.main.service.impl;

  import com.hehui.main.entity.User;
  import com.hehui.main.mapper.UserMapper;
  import com.hehui.main.service.UserService;
  import org.springframework.stereotype.Service;

  @Service
  public class UserServiceImpl implements UserService {

      private final UserMapper userMapper;

      public UserServiceImpl(UserMapper userMapper) {
          this.userMapper = userMapper;
      }

      @Override
      public User getById(Integer id) {
          return userMapper.selectById(id);
      }
  }