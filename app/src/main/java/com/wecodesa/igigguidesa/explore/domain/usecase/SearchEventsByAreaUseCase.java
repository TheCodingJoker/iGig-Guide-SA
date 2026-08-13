package com.wecodesa.igigguidesa.explore.domain.usecase;


import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.repository.EventRepository;
import javax.inject.Inject;
import java.util.List;
import io.reactivex.rxjava3.core.Single;

public class SearchEventsByAreaUseCase {

    private final EventRepository eventRepository;

    @Inject
    public SearchEventsByAreaUseCase(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Single<List<Event>> execute(String area) {
        return eventRepository.searchEventsByArea(area);
    }
}