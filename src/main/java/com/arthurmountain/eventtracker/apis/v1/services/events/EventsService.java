package com.arthurmountain.eventtracker.apis.v1.services.events;

import java.util.List;

import com.arthurmountain.eventtracker.apis.v1.entities.Event;

public interface EventsService {
  // 新增事件
  Event createEvent(String eventType, String userId, String metadata);

  // 取得某用戶的事件清單
  List<Event> getEventsByUserId(String userId);
}
