package org.example.eventy.users.models;

import jakarta.persistence.*;
import org.example.eventy.common.models.PicturePath;
import org.example.eventy.events.models.Event;
import org.example.eventy.solutions.models.Solution;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<PicturePath> imageUrls;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isDeactivated;

    @Column(nullable = false)
    private boolean hasSilencedNotifications;

    @Column()
    private LocalDateTime suspensionDeadline;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "BlockedUsers", joinColumns = @JoinColumn(name = "blocker_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "blocked_id", referencedColumnName = "id"))
    private List<User> blocked;


    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "UsersAttendingEvents", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    private List<Event> acceptedEvents;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "UsersFavoriteEvents", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    private List<Event> favoriteEvents;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "UsersFavoriteSolutions", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "solution_id", referencedColumnName = "id"))
    private List<Solution> favoriteSolutions;

    public User() {

    }

    public User(Long id, ArrayList<PicturePath> imageUrls, String email, String password, String address, String phoneNumber, boolean isActive, boolean isDeactivated, boolean hasSilencedNotifications, LocalDateTime suspensionDeadline) {
        this.id = id;
        this.imageUrls = imageUrls;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.isDeactivated = isDeactivated;
        this.hasSilencedNotifications = hasSilencedNotifications;
        this.suspensionDeadline = suspensionDeadline;
        this.blocked = new ArrayList<>();
        this.acceptedEvents = new ArrayList<>();
        this.favoriteEvents = new ArrayList<>();
        this.favoriteSolutions = new ArrayList<>();
    }

    public User(Long id, ArrayList<PicturePath> imageUrls, String email, String password, String address, String phoneNumber, boolean isActive, boolean isDeactivated, boolean hasSilencedNotifications, LocalDateTime suspensionDeadline, List<User> blocked, RegistrationRequest registrationRequest, List<Event> acceptedEvents, List<Event> favoriteEvents, List<Solution> favoriteSolutions) {
        this.id = id;
        this.imageUrls = imageUrls;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.isDeactivated = isDeactivated;
        this.hasSilencedNotifications = hasSilencedNotifications;
        this.suspensionDeadline = suspensionDeadline;
        this.blocked = blocked;
        this.acceptedEvents = acceptedEvents;
        this.favoriteEvents = favoriteEvents;
        this.favoriteSolutions = favoriteSolutions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PicturePath> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<PicturePath> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDeactivated() {
        return isDeactivated;
    }

    public void setDeactivated(boolean deactivated) {
        isDeactivated = deactivated;
    }

    public boolean isHasSilencedNotifications() {
        return hasSilencedNotifications;
    }

    public void setHasSilencedNotifications(boolean hasSilencedNotifications) {
        this.hasSilencedNotifications = hasSilencedNotifications;
    }

    public LocalDateTime getSuspensionDeadline() {
        return suspensionDeadline;
    }

    public void setSuspensionDeadline(LocalDateTime suspensionDeadline) {
        this.suspensionDeadline = suspensionDeadline;
    }

    public List<User> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<User> blocked) {
        this.blocked = blocked;
    }

    public List<Event> getAcceptedEvents() {
        return acceptedEvents;
    }

    public void setAcceptedEvents(List<Event> acceptedEvents) {
        this.acceptedEvents = acceptedEvents;
    }

    public List<Event> getFavoriteEvents() {
        return favoriteEvents;
    }

    public void setFavoriteEvents(List<Event> favoriteEvents) {
        this.favoriteEvents = favoriteEvents;
    }

    public List<Solution> getFavoriteSolutions() {
        return favoriteSolutions;
    }

    public void setFavoriteSolutions(List<Solution> favoriteSolutions) {
        this.favoriteSolutions = favoriteSolutions;
    }
}
