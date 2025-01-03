package org.example.eventy.users.dtos;

import jakarta.validation.constraints.NotNull;
import org.example.eventy.users.validation.annotation.ValidUpgradeProfile;

import java.util.List;

@ValidUpgradeProfile // trigger the custom validation
public class UpgradeProfileDTO {
    @NotNull(message = "Email cannot be null")
    private String encryptedEmail;

    @NotNull(message = "Account type cannot be null")
    private String accountType;

    private String firstName;
    private String lastName;

    private String companyName;
    private String description;

    // if null, set basic pic depending on the accountType
    private List<String> profilePictures;

    /////////////////////////////////////

    public UpgradeProfileDTO() {
    }

    public UpgradeProfileDTO(String encryptedEmail, String accountType, String firstName, String lastName, String companyName, String description, List<String> profilePictures) {
        this.encryptedEmail = encryptedEmail;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.description = description;
        this.profilePictures = profilePictures;
    }

    public String getEncryptedEmail() {
        return encryptedEmail;
    }

    public void setEncryptedEmail(String encryptedEmail) {
        this.encryptedEmail = encryptedEmail;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(List<String> profilePictures) {
        this.profilePictures = profilePictures;
    }
}
