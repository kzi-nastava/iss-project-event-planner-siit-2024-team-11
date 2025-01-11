package org.example.eventy.solutions.models;

import jakarta.persistence.*;
import org.example.eventy.common.models.ReservationConfirmationType;

@Entity
@Table(name = "ServiceHistory")
public class ServiceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int discount;

    @Column(nullable = false)
    private String specifics;

    @Column(nullable = false)
    private Integer minReservationTime;

    @Column(nullable = false)
    private Integer maxReservationTime;

    @Column(nullable = false)
    private Integer reservationDeadline;

    @Column(nullable = false)
    private Integer cancellationDeadline;

    @Column(nullable = false)
    private ReservationConfirmationType reservationType;

    @Column(nullable = false)
    private boolean isVisible;

    @Column(nullable = false)
    private boolean isAvailable;

    public ServiceHistory() {

    }

    public ServiceHistory(Long id, String name, String description, double price, int discount, String specifics, Integer minReservationTime, Integer maxReservationTime, Integer reservationDeadline, Integer cancellationDeadline, ReservationConfirmationType reservationType, boolean isVisible, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.specifics = specifics;
        this.minReservationTime = minReservationTime;
        this.maxReservationTime = maxReservationTime;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationType = reservationType;
        this.isVisible = isVisible;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
