package com.wecodesa.igigguidesa.explore.data.mapper;

import com.wecodesa.igigguidesa.core.domain.model.Venue;
import com.wecodesa.igigguidesa.explore.data.remote.dto.EventDto;
import com.wecodesa.igigguidesa.explore.data.remote.dto.VenueDto;
import com.wecodesa.igigguidesa.explore.domain.model.Event;
import com.wecodesa.igigguidesa.explore.domain.model.EventType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.inject.Inject;

public class EventMapper {
    @Inject
    public EventMapper() {
    }

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public Event mapToDomain(EventDto dto) {
        if (dto == null) {
            return null;
        }

        Event event = new Event();
        event.setId(dto.getId());
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartDateTime(parseDate(dto.getStartDateTime()));
        event.setEndDateTime(parseDate(dto.getEndDateTime()));
        event.setEventType(mapEventType(dto.getEventType()));
        event.setVenue(mapVenueToDomain(dto.getVenue()));
        event.setTicketPrice(dto.getTicketPrice());
        event.setFree(dto.isFree());
        event.setTicketPurchaseLink(dto.getTicketPurchaseLink());
        event.setImageUrl(dto.getImageUrl());

        return event;
    }

    private Venue mapVenueToDomain(VenueDto dto) {
        if (dto == null) {
            return null;
        }

        Venue venue = new Venue();
        venue.setId(dto.getId());
        venue.setName(dto.getName());
        venue.setAddress(dto.getAddress());
        venue.setCity(dto.getCity());
        venue.setProvince(dto.getProvince());
        venue.setLatitude(dto.getLatitude());
        venue.setLongitude(dto.getLongitude());

        return venue;
    }

    private EventType mapEventType(String rawType) {
        if (rawType == null) {
            return EventType.OTHER;
        }
        try {
            return EventType.valueOf(rawType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return EventType.OTHER;
        }
    }

    private Date parseDate(String rawDate) {
        if (rawDate == null || rawDate.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse(rawDate);
        } catch (ParseException e) {
            return null;
        }
    }
}