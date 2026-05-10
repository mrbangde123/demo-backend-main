package com.hehui.main.entity;

  import com.baomidou.mybatisplus.annotation.IdType;
  import com.baomidou.mybatisplus.annotation.TableId;
  import com.baomidou.mybatisplus.annotation.TableName;

  import java.time.LocalDateTime;

  @TableName("user")
  public class User {

      @TableId(type = IdType.AUTO)
      private Integer id;

      private String name;

      private Integer age;

      private LocalDateTime createdAt;

      // --- Getter / Setter ---
      public Integer getId() { return id; }
      public void setId(Integer id) { this.id = id; }

      public String getName() { return name; }
      public void setName(String name) { this.name = name; }

      public Integer getAge() { return age; }
      public void setAge(Integer age) { this.age = age; }

      public LocalDateTime getCreatedAt() { return createdAt; }
      public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  }