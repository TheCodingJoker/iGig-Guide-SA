package com.wecodesa.igigguidesa.explore.data.repository;

import com.wecodesa.igigguidesa.explore.data.mapper.EventMapper;
import com.wecodesa.igigguidesa.explore.data.remote.datasource.EventRemoteDataSource;
import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.model.EventType;
import com.wecodesa.igigguidesa.explore.domain.repository.EventRepository;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class EventRepositoryImpl implements EventRepository {

    private final EventRemoteDataSource eventRemoteDataSource;
    private final EventMapper eventMapper;

    @Inject
    public EventRepositoryImpl(EventRemoteDataSource eventRemoteDataSource, EventMapper eventMapper) {
        this.eventRemoteDataSource = eventRemoteDataSource;
        this.eventMapper = eventMapper;
    }

    @Override
    public Single<List<Event>> searchEventsByArea(String area) {
        return eventRemoteDataSource.searchEventsByCity(area)
                .map(eventDtoList -> {
                    List<Event> events = new ArrayList<>();
                    for (int i = 0; i < eventDtoList.size(); i++) {
                        events.add(eventMapper.mapToDomain(eventDtoList.get(i)));
                    }
                    return events;
                });
    }

    @Override
    public List<Event> filterEventsByType(List<Event> events, EventType eventType) {
        List<Event> filtered = new ArrayList<>();
        for (Event event : events) {
            if (event.getEventType() == eventType) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    @Override
    public Single<Event> getEventDetails(String eventId) {
        return eventRemoteDataSource.getEventById(eventId)
                .map(eventMapper::mapToDomain);
    }
}