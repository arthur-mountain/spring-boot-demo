package com.arthurmountain.eventtracker.apis.v1.services.events;

import com.arthurmountain.eventtracker.apis.v1.entities.RedisEvent;
import com.arthurmountain.eventtracker.apis.v1.entities.Event;
import com.arthurmountain.eventtracker.apis.v1.repositories.events.cache.RedisEventsCache;
import com.arthurmountain.eventtracker.apis.v1.repositories.events.EventsRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsServiceImpl implements EventsService {
  @Autowired
  private RedisEventsCache eventsCacheRepository;

  @Autowired
  private EventsRepository eventsRepository;

  // 建立事件，存 DB，並清除該用戶的快取
  @Override
  public Event createEvent(String eventType, String userId, String metadata) {
    Event event = new Event();
    event.setEventType(eventType);
    event.setUserId(userId);
    event.setMetadata(metadata);
    event.setTimestamp(LocalDateTime.now());
    Event saved = eventsRepository.save(event);

    // create new event for user, so evict user's cache
    eventsCacheRepository.evictUserRedisEvents(userId);

    return saved;
  }

  // 讀取某用戶的事件 (先查 cache => 未命中則查 DB => 寫入 cache)
  @Override
  public List<Event> getEventsByUserId(String userId) {
    // 1. 從 Redis 查詢
    List<RedisEvent> redisEvents = eventsCacheRepository.getUserRedisEvents(userId);
    if (!redisEvents.isEmpty()) {
      System.out.println("\nCache hit!");
      return convertToEvents(redisEvents);
    }
    // 2. 從資料庫查詢
    List<Event> dbEvents = eventsRepository.findByUserId(userId);

    // 3. 如果資料庫有結果，寫入 Redis
    if (!dbEvents.isEmpty()) {
      List<RedisEvent> redisEventsToCache = convertToRedisEvents(dbEvents);
      eventsCacheRepository.cacheUserRedisEvents(userId, redisEventsToCache);
    }

    return dbEvents;
  }

  private List<RedisEvent> convertToRedisEvents(List<Event> dbEvents) {
    return dbEvents.stream().map(this::convertToRedisEvent).toList();
  }

  private List<Event> convertToEvents(List<RedisEvent> redisEvents) {
    return redisEvents.stream().map(this::convertToEvent).toList();
  }

  private RedisEvent convertToRedisEvent(Event dbEvent) {
    RedisEvent redisEvent = new RedisEvent();
    redisEvent.setEventType(dbEvent.getEventType());
    redisEvent.setTimestamp(dbEvent.getTimestamp());
    redisEvent.setMetadata(dbEvent.getMetadata());
    return redisEvent;
  }

  private Event convertToEvent(RedisEvent redisEvent) {
    Event dbEvent = new Event();
    dbEvent.setEventType(redisEvent.getEventType());
    dbEvent.setTimestamp(redisEvent.getTimestamp());
    dbEvent.setMetadata(redisEvent.getMetadata());
    return dbEvent;
  }

}
