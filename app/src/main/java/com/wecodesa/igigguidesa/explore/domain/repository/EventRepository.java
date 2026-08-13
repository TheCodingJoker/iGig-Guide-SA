package com.wecodesa.igigguidesa.explore.domain.repository;

import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.model.EventType;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface EventRepository {

    Single<List<Event>> searchEventsByArea(String area);

    List<Event> filterEventsByType(List<Event> events, EventType eventType);

    Single<Event> getEventDetails(String eventId);
}