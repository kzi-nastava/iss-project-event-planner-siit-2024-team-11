package org.example.eventy.users.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.example.eventy.events.models.Event;

import java.util.ArrayList;
import java.util.List;

@Entity
public class EventOrganizer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // just current mocking so I can run and test the code
    private String firstName;
    private String lastName;

    public EventOrganizer() {

    }

    public EventOrganizer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public EventOrganizer(String firstName, String lastName, List<Event> organizedEvents) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}
