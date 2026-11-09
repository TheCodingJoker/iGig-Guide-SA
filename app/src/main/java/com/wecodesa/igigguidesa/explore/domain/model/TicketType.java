package com.wecodesa.igigguidesa.explore.domain.model;

import java.util.List;

public class TicketType {

    private String id;
    private String name;
    private double price;
    private List<String> perks;

    public TicketType() {
    }

    public TicketType(String id, String name, double price, List<String> perks) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.perks = perks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getPerks() {
        return perks;
    }

    public void setPerks(List<String> perks) {
        this.perks = perks;
    }
}