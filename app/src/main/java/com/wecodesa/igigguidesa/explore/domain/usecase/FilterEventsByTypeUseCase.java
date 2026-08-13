package com.wecodesa.igigguidesa.explore.domain.usecase;

import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.model.EventType;
import com.wecodesa.igigguidesa.explore.domain.repository.EventRepository;

import java.util.List;

import javax.inject.Inject;

public class FilterEventsByTypeUseCase {

    private final EventRepository eventRepository;

    @Inject
    public FilterEventsByTypeUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> execute(List<Event> events, EventType eventType) {
        return eventRepository.filterEventsByType(events, eventType);
    }
}