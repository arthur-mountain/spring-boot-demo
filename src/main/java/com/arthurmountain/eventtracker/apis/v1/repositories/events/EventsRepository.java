package com.arthurmountain.eventtracker.apis.v1.repositories.events;

import com.arthurmountain.eventtracker.apis.v1.entities.Event;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends CrudRepository<Event, Long> {
  @Query("SELECT * FROM events WHERE user_id = :userId ORDER BY timestamp DESC")
  List<Event> findByUserId(String userId);
}
