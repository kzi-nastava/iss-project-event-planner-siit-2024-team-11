package org.example.eventy.events.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class EventStatsDTO {
    private EventDTO event;
    private int visitors;
    private double averageGrade;

    public EventStatsDTO(EventDTO event) {

    }

    public EventStatsDTO(EventDTO event, int visitors, double averageGrade) {
        this.event = event;
        this.visitors = visitors;
        this.averageGrade = averageGrade;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }
}
