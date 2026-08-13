package com.wecodesa.igigguidesa.explore.domain.usecase;

import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.repository.EventRepository;
import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetEventDetailsUseCase {

    private final EventRepository eventRepository;

    @Inject
    public GetEventDetailsUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Single<Event> execute(String eventId) {
        return eventRepository.getEventDetails(eventId);
    }
}