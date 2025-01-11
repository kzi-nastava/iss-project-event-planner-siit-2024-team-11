package org.example.eventy.solutions.models;

import jakarta.persistence.*;
import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.models.ReservationConfirmationType;
import org.example.eventy.events.models.EventType;
import org.example.eventy.users.models.SolutionProvider;

import java.util.List;

@Entity
@DiscriminatorValue("Service")
public class Service extends Solution {

    // NOTE: we cannot put @Column(nullable = FALSE) because of SingeTable,
    //       so these will all be NULL if it's Product

    @Column
    private String specifics;

    @Column
    private Integer minReservationTime;

    @Column
    private Integer maxReservationTime;

    @Column
    private Integer reservationDeadline;

    @Column
    private Integer cancellationDeadline;

    @Column
    private ReservationConfirmationType reservationType;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "service_history_id", referencedColumnName = "id")
    private ServiceHistory currentService;

    ////////////////////////////////////////////

    public Service() {

    }

    public Service(Long id, String name, String description, double price, int discount, List<PicturePath> imageUrls, boolean isVisible, boolean isAvailable, boolean isDeleted, Category category, List<EventType> relatedEventTypes, SolutionProvider provider, String specifics, Integer minReservationTime, Integer maxReservationTime, Integer reservationDeadline, Integer cancellationDeadline, ReservationConfirmationType reservationType, ServiceHistory currentService) {
        super(id, name, description, price, discount, imageUrls, isVisible, isAvailable, isDeleted, category, relatedEventTypes, provider);
        this.specifics = specifics;
        this.minReservationTime = minReservationTime;
        this.maxReservationTime = maxReservationTime;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationType = reservationType;
        this.currentService = currentService;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    public Integer getMinReservationTime() {
        return minReservationTime;
    }

    public void setMinReservationTime(Integer minReservationTime) {
        this.minReservationTime = minReservationTime;
    }

    public Integer getMaxReservationTime() {
        return maxReservationTime;
    }

    public void setMaxReservationTime(Integer maxReservationTime) {
        this.maxReservationTime = maxReservationTime;
    }

    public Integer getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(Integer reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public ReservationConfirmationType getReservationType() {
        return reservationType;
    }

    public void setReservationType(ReservationConfirmationType reservationType) {
        this.reservationType = reservationType;
    }

    public ServiceHistory getCurrentService() {
        return currentService;
    }

    public void setCurrentService(ServiceHistory currentService) {
        this.currentService = currentService;
    }
}