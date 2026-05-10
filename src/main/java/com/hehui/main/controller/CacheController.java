package com.hehui.main.controller;

  import org.springframework.data.redis.core.StringRedisTemplate;
  import org.springframework.web.bind.annotation.*;

  import java.util.concurrent.TimeUnit;

  @RestController
  @RequestMapping("/api/cache")
  public class CacheController {

      /**
       * redisTemplate.opsForValue()    // String    → SET/GET
       *   redisTemplate.opsForHash()     // Hash      → HSET/HGET
       *   redisTemplate.opsForList()     // List      → LPUSH/RPOP
       *   redisTemplate.opsForSet()      // Set       → SADD/SMEMBERS
       *   redisTemplate.opsForZSet()     // Sorted Set → ZADD/ZRANGE
       */

      private final StringRedisTemplate redisTemplate;

      public CacheController(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
      }

      // 写入（带 5 分钟过期时间）
      @PostMapping("/{key}")
      public String set(@PathVariable String key, @RequestParam String value) {
          redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
          return "OK: " + key + " -> " + value;
      }

      // 读出
      @GetMapping("/{key}")
      public String get(@PathVariable String key) {
          String value = redisTemplate.opsForValue().get(key);
          return value != null ? value : "(not found)";
      }

      // 删除（多送一个接口，真实项目常用）
      @DeleteMapping("/{key}")
      public String delete(@PathVariable String key) {
          Boolean removed = redisTemplate.delete(key);
          return Boolean.TRUE.equals(removed) ? "DELETED" : "NOT FOUND";
      }
  }