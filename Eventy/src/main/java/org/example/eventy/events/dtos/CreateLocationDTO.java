package org.example.eventy.events.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

public class CreateLocationDTO {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Address cannot be empty")
    private String address;
    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90")
    private double latitude;
    @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180")
    private double longitude;

    public CreateLocationDTO() {

    }

    public CreateLocationDTO(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
