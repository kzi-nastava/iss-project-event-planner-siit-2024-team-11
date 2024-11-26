package org.example.eventy.solutions.dtos.services;

import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.dtos.CategoryDTO;

import java.util.Collection;

public class CreateServiceDTO {

    private String name;
    private String description;
    private double price;
    private int discount;
    private byte[][] images;
    private CategoryDTO category;
    private Collection<EventTypeDTO> relatedEventTypes;
    private String specifics;
    private int minReservationTime;
    private int maxReservationTime;
    private int reservationDeadline;
    private int cancellationDeadline;
    private boolean automaticReservationAcceptance;

    public CreateServiceDTO() { super(); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public byte[][] getImages() {
        return images;
    }

    public void setImages(byte[][] images) {
        this.images = images;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Collection<EventTypeDTO> getRelatedEventTypes() {
        return relatedEventTypes;
    }

    public void setRelatedEventTypes(Collection<EventTypeDTO> relatedEventTypes) {
        this.relatedEventTypes = relatedEventTypes;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    public int getMinReservationTime() {
        return minReservationTime;
    }

    public void setMinReservationTime(int minReservationTime) {
        this.minReservationTime = minReservationTime;
    }

    public int getMaxReservationTime() {
        return maxReservationTime;
    }

    public void setMaxReservationTime(int maxReservationTime) {
        this.maxReservationTime = maxReservationTime;
    }

    public int getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(int reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public boolean getAutomaticReservationAcceptance() {
        return automaticReservationAcceptance;
    }

    public void setAutomaticReservationAcceptance(boolean automaticReservationAcceptance) {
        this.automaticReservationAcceptance = automaticReservationAcceptance;
    }
}
