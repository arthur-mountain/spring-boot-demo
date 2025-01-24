package com.arthurmountain.eventtracker.apis.v1.controllers;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.arthurmountain.eventtracker.apis.v1.dtos.EventDto;
import com.arthurmountain.eventtracker.apis.v1.entities.Event;
import com.arthurmountain.eventtracker.apis.v1.services.events.EventsService;

@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
  @Autowired
  private EventsService eventsService;

  // 建立事件
  @PostMapping
  public ResponseEntity<Event> createEvent(
      @RequestBody @Valid EventDto event) {
    Event saved = eventsService.createEvent(
        event.getEventType(),
        event.getUserId(),
        event.getMetadata());
    return ResponseEntity.ok(saved);
  }

  // 查詢特定用戶
  @GetMapping("/users/{userId}")
  public ResponseEntity<List<Event>> getUserEvents(@PathVariable String userId) {
    List<Event> events = eventsService.getEventsByUserId(userId);
    return ResponseEntity.ok(events);
  }
}
