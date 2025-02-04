package org.example.eventy.solutions.dtos.services;

import org.example.eventy.events.dtos.EventTypeDTO;
import org.example.eventy.solutions.dtos.CategoryDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateServiceDTO {

    private String name;
    private String description;
    private double price;
    private double discount;
    private ArrayList<String> imageUrls;
    private Long providerId;
    private Long categoryId;
    private List<Long> relatedEventTypeIds;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getRelatedEventTypeIds() {
        return relatedEventTypeIds;
    }

    public void setRelatedEventTypeIds(List<Long> relatedEventTypeIds) {
        this.relatedEventTypeIds = relatedEventTypeIds;
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

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public boolean isAutomaticReservationAcceptance() {
        return automaticReservationAcceptance;
    }
}
