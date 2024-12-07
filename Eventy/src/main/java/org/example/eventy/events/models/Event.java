package org.example.eventy.events.models;

import jakarta.persistence.*;
import org.example.eventy.users.models.EventOrganizer;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int maxNumberParticipants;
    @Column(nullable = false)
    private PrivacyType privacy;
    @Column(nullable = false)
    private LocalDateTime date;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "event_type_id", referencedColumnName = "id")
    private EventType type;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private List<Activity> agenda;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "organizer_id", referencedColumnName = "fakeId")
    private EventOrganizer organiser;

    public Event() {

    }

    public Event(Long id, String name, String description, int maxNumberParticipants, PrivacyType privacy, LocalDateTime date, EventType type, Location location, List<Activity> agenda, EventOrganizer organiser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxNumberParticipants = maxNumberParticipants;
        this.privacy = privacy;
        this.date = date;
        this.type = type;
        this.location = location;
        this.agenda = agenda;
        this.organiser = organiser;
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

    public int getMaxNumberParticipants() {
        return maxNumberParticipants;
    }

    public void setMaxNumberParticipants(int maxNumberParticipants) {
        this.maxNumberParticipants = maxNumberParticipants;
    }

    public PrivacyType getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacyType privacy) {
        this.privacy = privacy;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Activity> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<Activity> agenda) {
        this.agenda = agenda;
    }

    public EventOrganizer getOrganiser() {
        return organiser;
    }

    public void setOrganiser(EventOrganizer organiser) {
        this.organiser = organiser;
    }
}
