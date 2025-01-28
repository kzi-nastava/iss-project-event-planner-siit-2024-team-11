package org.example.eventy.users.dtos;

import jakarta.validation.constraints.NotNull;
import org.example.eventy.users.validation.annotation.ValidFastRegistration;

@ValidFastRegistration // trigger the custom validation
public class FastRegistrationDTO {
    @NotNull(message = "Email cannot be null")
    private String encryptedEmail;

    @NotNull(message = "Password cannot be null")
    private String password;

    @NotNull(message = "Confirmed password cannot be null")
    private String confirmedPassword;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;

    public FastRegistrationDTO() {}

    public FastRegistrationDTO(String encryptedEmail, String password, String confirmedPassword, String address, String phoneNumber) {
        this.encryptedEmail = encryptedEmail;
        this.password = password;
        this.confirmedPassword = confirmedPassword;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getEncryptedEmail() {
        return encryptedEmail;
    }

    public void setEncryptedEmail(String encryptedEmail) {
        this.encryptedEmail = encryptedEmail;
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
