package org.example.eventy.users.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Organizer")
public class EventOrganizer extends User {
    @Column()
    private String firstName;
    @Column()
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
