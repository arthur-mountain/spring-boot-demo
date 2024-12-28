package com.arthurmountain.eventtracker.apis.v1.repositories.events.cache;

import com.arthurmountain.eventtracker.apis.v1.entities.RedisEvent;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisEventsCacheImpl implements RedisEventsCache {

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Autowired
  private ObjectMapper objectMapper; // 用於處理 JSON 序列化與反序列化

  public RedisEventsCacheImpl() {
  }

  @Override
  public void cacheUserRedisEvents(String userId, List<RedisEvent> events) {
    String key = buildKey(userId);

    // 先移除舊的 list
    redisTemplate.delete(key);

    // 將每個 RedisEvent 轉成 JSON 字串並存儲到 Redis 中
    ListOperations<String, String> listOps = redisTemplate.opsForList();
    for (RedisEvent event : events) {
      try {
        // 使用 Jackson 將 RedisEvent 物件序列化為 JSON 字串
        listOps.rightPush(key, objectMapper.writeValueAsString(event));
      } catch (Exception e) {
        e.printStackTrace(); // catch 序列化過程中的異常
      }
    }
  }

  @Override
  public List<RedisEvent> getUserRedisEvents(String userId) {
    String key = buildKey(userId);

    // 取得整個 list
    List<String> lines = redisTemplate.opsForList().range(key, 0, -1);

    // 轉回 RedisEvent list
    List<RedisEvent> events = new ArrayList<>();

    if (lines == null || lines.isEmpty()) {
      return events;
    }

    for (String line : lines) {
      try {
        // 使用 Jackson 反序列化 JSON 字串為 RedisEvent 物件
        RedisEvent event = objectMapper.readValue(line, RedisEvent.class);
        if (event != null) {
          events.add(event);
        }
      } catch (Exception e) {
        e.printStackTrace(); // catch 反序列化過程中的異常
      }
    }
    return events;
  }

  @Override
  public void evictUserRedisEvents(String userId) {
    redisTemplate.delete(buildKey(userId));
  }

  // 產生 Redis key
  private String buildKey(String userId) {
    return "events:" + userId;
  }
}
