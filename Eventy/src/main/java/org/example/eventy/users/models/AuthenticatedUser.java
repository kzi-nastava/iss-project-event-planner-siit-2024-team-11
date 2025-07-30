package org.example.eventy.users.models;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("AuthenticatedUser")
public class AuthenticatedUser extends User {

    public AuthenticatedUser() {

    }
}
