package org.example.eventy.solutions.models;

import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.events.models.EventType;

import java.util.List;

public class Service extends Solution {
    private ServiceHistory currentService;

    private String specifics;
    private int minReservationTime;
    private int maxReservationTime;
    private int reservationDeadline;
    private int cancellationDeadline;
    private ReservationConfirmationType reservationType;

    public Service() {

    }

    public Service(Long id, String name, String description, double price, int discount, byte[][] images, boolean isVisible, boolean isAvailable, boolean isDeleted, Category category, List<EventType> relatedEventTypes, ServiceHistory currentService, String specifics, int minReservationTime, int maxReservationTime, int reservationDeadline, int cancellationDeadline, ReservationConfirmationType reservationType) {
        super(id, name, description, price, discount, images, isVisible, isAvailable, isDeleted, category, relatedEventTypes);
        this.currentService = currentService;
        this.specifics = specifics;
        this.minReservationTime = minReservationTime;
        this.maxReservationTime = maxReservationTime;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationType = reservationType;
    }

    public Service(String specifics, int minReservationTime, int maxReservationTime, int reservationDeadline, int cancellationDeadline, ReservationConfirmationType reservationType) {
        this.specifics = specifics;
        this.minReservationTime = minReservationTime;
        this.maxReservationTime = maxReservationTime;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationType = reservationType;
    }

    public Service(ServiceHistory currentService) {
        this.currentService = currentService;
    }

    public ServiceHistory getCurrentService() {
        return currentService;
    }

    public void setCurrentService(ServiceHistory currentService) {
        this.currentService = currentService;
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

    public ReservationConfirmationType getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationConfirmationType reservationType) {
        this.reservationType = reservationType;
    }
}
