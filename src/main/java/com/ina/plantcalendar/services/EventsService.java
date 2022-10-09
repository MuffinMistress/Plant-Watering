package com.ina.plantcalendar.services;

import com.ina.plantcalendar.database.IDataSource;
import com.ina.plantcalendar.model.AggregatedEventsPerDay;
import com.ina.plantcalendar.database.DataSource;
import com.ina.plantcalendar.model.Event;
import com.ina.plantcalendar.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Lists;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class EventsService implements IEventsService {

    private ArrayList<Event> events = new ArrayList<>();
    private final IDataSource dataSource;

    @Autowired
    public EventsService(IDataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
    }

    // TODO I need to figure out what will happen if there are several plants under the name;
    public void addRecurringEvent(String plantScientificName, Event.EventType eventType, LocalDate lastWateredOn, LocalDate endDate) throws SQLException {
        Plant plant = dataSource.queryPlantByExactScientificName(plantScientificName);

        if (plant == null) {
            System.out.println("Plant not in the DB.");
            return;
        }

        if (dataSource.isEventInDB(plantScientificName, eventType)) {
            return;
        } else {
            Event event = new Event(plant, eventType, lastWateredOn);
            dataSource.addRecurringEvent(dataSource.queryPlantIdByScientificName(plant.getScientificName()), eventType, lastWateredOn, event.getEventDate(), endDate);
        }

        // TODO Add logic for changing the event
    }

    public void addRecurringEvent(String plantScientificName, Event.EventType eventType, LocalDate lastWateredOn) throws SQLException {
        Plant plant = dataSource.queryPlantByExactScientificName(plantScientificName);

        if (plant == null) {
            System.out.println("Plant not in the DB.");
            return;
        }

        if (dataSource.isEventInDB(plantScientificName, eventType)) {
            return;
        } else {
            Event event = new Event(plant, eventType, lastWateredOn);
            dataSource.addRecurringEvent(dataSource.queryPlantIdByScientificName(plant.getScientificName()), eventType, lastWateredOn, event.getEventDate());
        }

        // TODO Add logic for changing the event
    }

    // TODO Get rid of this and connect everything to DB
    public void addRecurringEventTest(Plant plant, Event.EventType eventType, LocalDate lastWateredOn) {
        Event event = new Event(plant, eventType, lastWateredOn);
        events.add(event);
    }

    public List<AggregatedEventsPerDay> getUpcomingAggregatedEventsForTheUpcomingWeek() {
        // TODO implement dataSource.findAllEventsByDate();
        List<Event> allFoundEvents = dataSource.findAllEventsByDate(LocalDate.now(), LocalDate.now().plusDays(7));
        List<AggregatedEventsPerDay> allEventsForTheWeek = new ArrayList<>();
        Map<LocalDate, List<Event>> eventByDay = allFoundEvents.stream().collect(Collectors.groupingBy(Event::getEventDate));

        for (var entry : eventByDay.entrySet()) {
            List<Event> events = entry.getValue();
            List<Plant> plants = new ArrayList<>();
            for (Event event : events) {
                Plant plant = event.getPlant();
                plants.add(plant);
            }
            AggregatedEventsPerDay eventsPerDay = new AggregatedEventsPerDay(entry.getKey(), Event.EventType.WATERING, plants);
            allEventsForTheWeek.add(eventsPerDay);
        }
        return allEventsForTheWeek.stream()
                .sorted(Comparator.comparing(AggregatedEventsPerDay::getDate))
                .collect(Collectors.toList());
    }
}
