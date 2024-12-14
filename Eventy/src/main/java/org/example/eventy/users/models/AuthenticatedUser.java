package org.example.eventy.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AuthenticatedUser")
public class AuthenticatedUser extends User {
    @Column()
    private String firstName;
    @Column()
    private String lastName;

    public AuthenticatedUser() {

    }

    public AuthenticatedUser(String firstName, String lastName) {
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
