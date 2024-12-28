package com.arthurmountain.eventtracker.apis.v1.repositories.events.cache;

import com.arthurmountain.eventtracker.apis.v1.entities.Event;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisEventsCacheImpl implements RedisEventsCache {

  @Autowired
  private StringRedisTemplate redisTemplate;

  public RedisEventsCacheImpl() {
  }

  @Override
  public void cacheUserEvents(String userId, List<Event> eventList) {
    String key = buildKey(userId);

    // 先移除舊的 list
    redisTemplate.delete(key);

    // 將每個 Event 轉成一行字串，使用 | 作為分隔符
    for (Event event : eventList) {
      redisTemplate.opsForList().rightPush(key, convertEventToLine(event));
    }
  }

  @Override
  public List<Event> getUserEvents(String userId) {
    String key = buildKey(userId);

    // 取得整個 list
    List<String> lines = redisTemplate.opsForList().range(key, 0, -1);
    if (lines == null || lines.isEmpty()) {
      return new ArrayList<>();
    }

    // 轉回 Event
    List<Event> result = new ArrayList<>();
    for (String line : lines) {
      Event e = convertLineToEvent(line);
      if (e != null) {
        result.add(e);
      }
    }
    return result;
  }

  @Override
  public void evictUserEvents(String userId) {
    redisTemplate.delete(buildKey(userId));
  }

  // 產生 Redis key
  private String buildKey(String userId) {
    return "events:" + userId;
  }

  // 將 Event -> "eventType|timestamp|metadata"
  private String convertEventToLine(Event event) {
    StringBuilder sb = new StringBuilder();
    sb.append(event.getEventType() == null ? "" : event.getEventType()).append("|");
    sb.append(event.getTimestamp() == null ? "" : event.getTimestamp().toString()).append("|");
    sb.append(event.getMetadata() == null ? "" : event.getMetadata());
    return sb.toString();
  }

  // 將字串反解析成 Event (若有更多欄位, 額外處理)
  private Event convertLineToEvent(String line) {
    String[] parts = line.split("\\|", 3); // 最多切3個區塊
    if (parts.length < 3) {
      return null;
    }
    String eventType = parts[0];
    String timestampStr = parts[1];
    String metadata = parts[2];

    LocalDateTime dt = null;
    if (!timestampStr.isEmpty()) {
      // 預設ISO-8601格式, 需注意 parse 失敗時的處理
      dt = LocalDateTime.parse(timestampStr);
    }

    Event e = new Event();
    e.setEventType(eventType);
    e.setTimestamp(dt);
    e.setMetadata(metadata);
    return e;
  }
}
