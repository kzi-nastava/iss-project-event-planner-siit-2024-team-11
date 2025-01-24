package org.example.eventy.users.dtos;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.services.PictureService;
import org.example.eventy.users.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private Long id;
    private List<String> profilePictures;
    private UserType userType;
    private String email;
    private String firstName;
    private String lastName;
    private String name;
    private String description;
    private String address;
    private String phoneNumber;

    public UserDTO() {

    }

    public UserDTO(User user, UserType userType) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userType = userType;
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();

        if (userType == UserType.PROVIDER) {
            this.name = ((SolutionProvider) user).getName();
            this.description = ((SolutionProvider) user).getDescription();
        } else if (userType == UserType.ORGANIZER) {
            this.firstName = ((EventOrganizer) user).getFirstName();
            this.lastName = ((EventOrganizer) user).getLastName();
        } else if (userType == UserType.ADMIN) {
            this.firstName = ((Admin) user).getFirstName();
            this.lastName = ((Admin) user).getLastName();
        } else {
            //this.firstName = ((AuthenticatedUser) user).getFirstName();
            //this.lastName = ((AuthenticatedUser) user).getLastName();
            this.firstName = "No first name for authenticated user in UserDTO.java - Tamara";
            this.lastName = "No last name for authenticated user in UserDTO.java - Tamara";
        }

        this.profilePictures = user.getImageUrls().stream().map(PicturePath::getPath).map(PictureService::getImage).collect(Collectors.toList());
    }

    public UserDTO(Long id, List<String> profilePictures, UserType userType, String email, String firstName, String lastName, String name, String description, String address, String phoneNumber) {
        this.id = id;
        this.profilePictures = profilePictures;
        this.userType = userType;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(List<String> profilePictures) {
        this.profilePictures = profilePictures;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
