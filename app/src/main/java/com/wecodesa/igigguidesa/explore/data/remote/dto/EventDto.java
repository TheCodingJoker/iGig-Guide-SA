package com.wecodesa.igigguidesa.explore.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventDto {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("start_datetime")
    private String startDateTime;

    @SerializedName("end_datetime")
    private String endDateTime;

    @SerializedName("event_type")
    private String eventType;

    @SerializedName("venues")
    private VenueDto venue;

    @SerializedName("ticket_purchase_link")
    private String ticketPurchaseLink;

    @SerializedName("image_url")
    private String imageUrl;

    public EventDto() {
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

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public VenueDto getVenue() {
        return venue;
    }

    public void setVenue(VenueDto venue) {
        this.venue = venue;
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

    @SerializedName("ticket_types")
    private List<TicketTypeDto> ticketTypes;

    public List<TicketTypeDto> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeDto> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}