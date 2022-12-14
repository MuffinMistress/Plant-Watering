package com.ina.plantcalendar.model;

import java.time.LocalDate;

public class Event {

    private Plant plant;
    private EventType eventType;
    private LocalDate eventDate;

    public enum EventType {
        WATERING, FERTILIZING, REPLANTING
    }

    public Event() {
    }

    public Event(Plant plant, EventType eventType, LocalDate eventDate) {
        this.plant = plant;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }

    // TODO Change to abstract class, to change behaviour depending on what type of event we have.

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        String dayOfTheWeek = eventDate.getDayOfWeek().toString();
        String dayOfTheWeekCapitalized = dayOfTheWeek.substring(0,1) + dayOfTheWeek.substring(1).toLowerCase();
        String dateDayMonthCapitalized = eventDate.getDayOfMonth() + " " + eventDate.getMonth().toString().substring(0,1) + eventDate.getMonth().toString().substring(1).toLowerCase();
        String eventTypeCapitalized = eventType.toString().substring(0,1) + eventType.toString().substring(1).toLowerCase();
        return  dayOfTheWeekCapitalized + ", " + dateDayMonthCapitalized + " | " + eventTypeCapitalized + ": " + plant.getAlias();
    }
}
