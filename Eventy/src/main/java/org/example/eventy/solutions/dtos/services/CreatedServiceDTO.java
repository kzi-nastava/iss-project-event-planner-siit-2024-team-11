package org.example.eventy.solutions.dtos.services;

import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.dtos.CategoryDTO;
import org.example.eventy.solutions.models.Service;

import java.util.ArrayList;
import java.util.Collection;

public class CreatedServiceDTO {

    private long id;
    private String name;
    private String description;
    private double price;
    private int discount;
    private byte[][] images;
    private boolean isVisible;
    private boolean isAvailable;
    private CategoryDTO category;
    private Collection<EventTypeDTO> relatedEventTypes;
    private String specifics;
    private int minReservationTime;
    private int maxReservationTime;
    private int reservationDeadline;
    private int cancellationDeadline;
    private boolean automaticReservationAcceptance;

    public CreatedServiceDTO() { super(); }

    public CreatedServiceDTO(Service service) {
        this.id = service.getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.price = service.getPrice();
        this.discount = service.getDiscount();
        this.images = service.getImages();
        this.isVisible = service.isVisible();
        this.isAvailable = service.isAvailable();
        this.category = new CategoryDTO(service.getCategory());
        this.relatedEventTypes = new ArrayList<>();
        service.getRelatedEventTypes().forEach(et -> this.relatedEventTypes.add(new EventTypeDTO(et.getName(), et.getDescription())));
        this.specifics = service.getSpecifics();
        this.minReservationTime = service.getMinReservationTime();
        this.maxReservationTime = service.getMaxReservationTime();
        this.reservationDeadline = service.getReservationDeadline();
        this.cancellationDeadline = service.getCancellationDeadline();
        this.automaticReservationAcceptance = service.getReservationType() == ReservationConfirmationType.AUTOMATIC;
    }

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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
