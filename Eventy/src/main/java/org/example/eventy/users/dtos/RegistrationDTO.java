package org.example.eventy.users.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.example.eventy.users.validation.annotation.ValidRegistration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ValidRegistration
public class RegistrationDTO {
    @NotNull(message = "Profile picture or profile pictures cannot be null")
    private List<String> profilePictures;
    @NotEmpty(message = "Email cannot be null")
    private String email;
    @NotEmpty(message = "Password cannot be null")
    private String password;
    @NotEmpty(message = "Confirmed password cannot be null")
    private String confirmedPassword;
    private String firstName;
    private String lastName;
    private String name;
    private String description;
    @NotEmpty(message = "Address cannot be null")
    private String address;
    @NotEmpty(message = "Phone number cannot be null")
    private String phoneNumber;

    public RegistrationDTO() {

    }

    public RegistrationDTO(List<String> profilePictures, String email, String password, String confirmedPassword, String firstName, String lastName, String name, String description, String address, String phoneNumber) {
        this.profilePictures = profilePictures;
        this.email = email;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public List<String> getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(List<String> profilePictures) {
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

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
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
