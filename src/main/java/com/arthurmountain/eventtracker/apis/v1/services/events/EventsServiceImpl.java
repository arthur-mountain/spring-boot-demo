package com.arthurmountain.eventtracker.apis.v1.services.events;

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
  private EventsRepository eventsRepository;

  @Autowired
  private RedisEventsCache eventsCacheRepository;

  /**
   * 建立事件，存 DB，並清除該用戶的快取
   */
  @Override
  public Event createEvent(String eventType, String userId, String metadata) {
    Event event = new Event();
    event.setEventType(eventType);
    event.setUserId(userId);
    event.setMetadata(metadata);
    event.setTimestamp(LocalDateTime.now());

    Event saved = eventsRepository.save(event);

    // create new event for user, so evict user's cache
    eventsCacheRepository.evictUserEvents(userId);

    return saved;
  }

  /**
   * 讀取某用戶的事件 (先查 cache => 未命中則查 DB => 寫入 cache)
   */
  @Override
  public List<Event> getEventsByUserId(String userId) {
    // 1. 查 Redis
    List<Event> events = eventsCacheRepository.getUserEvents(userId);
    if (!events.isEmpty()) {
      return events;
    }
    // 2. 查 DB
    events = eventsRepository.findByUserId(userId);
    // 3. 寫回 cache
    if (!events.isEmpty()) {
      eventsCacheRepository.cacheUserEvents(userId, events);
    }
    return events;
  }
}
