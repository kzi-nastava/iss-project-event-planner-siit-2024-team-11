package org.example.eventy.users.models;

import jakarta.persistence.*;

@Entity
public class EventOrganizer extends User {
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    public EventOrganizer() {

    }

    public EventOrganizer(String firstName, String lastName) {
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
