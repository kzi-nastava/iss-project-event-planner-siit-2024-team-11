package org.example.eventy.users.models;

import org.example.eventy.events.models.Event;
import org.example.eventy.solutions.models.Solution;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private byte[][] profilePictures;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private boolean isActive;
    private boolean isDeactivated;
    private boolean hasSilencedNotifications;
    private LocalDateTime suspensionDeadline;
    private List<User> blocked;
    private RegistrationRequest registrationRequest;
    private List<Event> acceptedEvents;
    private List<Event> favoriteEvents;
    private List<Solution> favoriteSolutions;

    public User() {

    }

    public User(Long id, byte[][] profilePictures, String email, String password, String address, String phoneNumber, boolean isActive, boolean isDeactivated, boolean hasSilencedNotifications, LocalDateTime suspensionDeadline) {
        this.id = id;
        this.profilePictures = profilePictures;
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

    public User(Long id, byte[][] profilePictures, String email, String password, String address, String phoneNumber, boolean isActive, boolean isDeactivated, boolean hasSilencedNotifications, LocalDateTime suspensionDeadline, List<User> blocked, RegistrationRequest registrationRequest, List<Event> acceptedEvents, List<Event> favoriteEvents, List<Solution> favoriteSolutions) {
        this.id = id;
        this.profilePictures = profilePictures;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.isDeactivated = isDeactivated;
        this.hasSilencedNotifications = hasSilencedNotifications;
        this.suspensionDeadline = suspensionDeadline;
        this.blocked = blocked;
        this.registrationRequest = registrationRequest;
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

    public byte[][] getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(byte[][] profilePictures) {
        this.profilePictures = profilePictures;
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

    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
    }

    public void setRegistrationRequest(RegistrationRequest registrationRequest) {
        this.registrationRequest = registrationRequest;
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
