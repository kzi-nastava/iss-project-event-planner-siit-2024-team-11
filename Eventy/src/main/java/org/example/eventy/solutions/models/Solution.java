package org.example.eventy.solutions.models;

import org.example.eventy.events.models.EventType;
import org.example.eventy.users.models.SolutionProvider;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private Long id;
    private String name;
    private Category category;
    private String description;
    private double price;
    private int discount;
    private ArrayList<String> imageUrls;
    private boolean isVisible;
    private boolean isAvailable;
    private boolean isDeleted;
    private List<EventType> eventTypes;
    private SolutionProvider provider;

    public Solution() {

    }

    public Solution(Long id, String name, String description, double price, int discount, ArrayList<String> imageUrls, boolean isVisible, boolean isAvailable, boolean isDeleted, Category category, List<EventType> eventTypes, SolutionProvider provider) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageUrls = imageUrls;
        this.isVisible = isVisible;
        this.isAvailable = isAvailable;
        this.isDeleted = isDeleted;
        this.category = category;
        this.eventTypes = eventTypes;
        this.provider = provider;
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

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public SolutionProvider getProvider() {
        return provider;
    }

    public void setProvider(SolutionProvider provider) {
        this.provider = provider;
    }
}
