package com.wecodesa.igigguidesa.explore.data.remote.datasource;

import com.wecodesa.igigguidesa.explore.data.remote.api.SupabaseEventApi;
import com.wecodesa.igigguidesa.explore.data.remote.dto.EventDto;

import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.rxjava3.core.Single;
import javax.inject.Inject;

public class EventRemoteDataSource {

    private final SupabaseEventApi api;

    @Inject
    public EventRemoteDataSource(SupabaseEventApi api) {
        this.api = api;
    }

    public Single<List<EventDto>> searchEventsByCity(String city) {
        String filter = "ilike.*" + city + "*";
        return api.searchEventsByCity(filter);
    }

    public Single<EventDto> getEventById(String eventId) {
        String filter = "eq." + eventId;
        return api.getEventById(filter)
                .map(eventDtoList -> {
                    if (eventDtoList.isEmpty()) {
                        throw new NoSuchElementException("Event not found for id: " + eventId);
                    }
                    return eventDtoList.get(0);
                });
    }
}