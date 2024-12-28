package com.arthurmountain.eventtracker.apis.v1.repositories.events.cache;

import com.arthurmountain.eventtracker.apis.v1.entities.Event;

import java.util.List;

public interface RedisEventsCache {
  // 把某用戶事件清單放到快取 (可覆蓋或追加, 視需求)
  void cacheUserEvents(String userId, List<Event> eventList);

  // 取出某用戶的事件清單
  List<Event> getUserEvents(String userId);

  // 移除某用戶事件清單
  void evictUserEvents(String userId);
}