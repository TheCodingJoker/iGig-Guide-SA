package com.wecodesa.igigguidesa.explore.domain.model;

import com.wecodesa.igigguidesa.core.domain.model.Venue;

import java.util.Date;

public class Event {

    private String id;
    private String title;
    private String description;
    private Date startDateTime;
    private Date endDateTime;
    private EventType eventType;
    private Venue venue;
    private double ticketPrice;
    private boolean isFree;
    private String ticketPurchaseLink;
    private String imageUrl;

    public Event() {
    }

    public Event(String id, String title, String description, Date startDateTime,
                 Date endDateTime, EventType eventType, Venue venue,
                 double ticketPrice, boolean isFree, String ticketPurchaseLink,
                 String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.eventType = eventType;
        this.venue = venue;
        this.ticketPrice = ticketPrice;
        this.isFree = isFree;
        this.ticketPurchaseLink = ticketPurchaseLink;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getTicketPurchaseLink() {
        return ticketPurchaseLink;
    }

    public void setTicketPurchaseLink(String ticketPurchaseLink) {
        this.ticketPurchaseLink = ticketPurchaseLink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}