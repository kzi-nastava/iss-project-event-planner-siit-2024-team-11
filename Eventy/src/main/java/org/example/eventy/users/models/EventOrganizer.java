package org.example.eventy.users.models;

import org.example.eventy.events.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventOrganizer extends User {
    private String firstName;
    private String lastName;
    private List<Event> organizedEvents;

    public EventOrganizer() {

    }

    public EventOrganizer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organizedEvents = new ArrayList<>();
    }

    public EventOrganizer(String firstName, String lastName, List<Event> organizedEvents) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organizedEvents = organizedEvents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(List<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }
}
