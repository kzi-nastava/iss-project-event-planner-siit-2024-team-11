package org.example.eventy.users.dtos;

import jakarta.validation.constraints.NotNull;
import org.example.eventy.users.validation.annotation.ValidUpgradeProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ValidUpgradeProfile // trigger the custom validation
public class UpgradeProfileDTO {
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Account type cannot be null")
    private String accountType;

    private String firstName;
    private String lastName;

    private String companyName;
    private String description;

    @NotNull(message = "Picture cannot be null")
    private List<MultipartFile> profilePictures;

    /////////////////////////////////////

    public UpgradeProfileDTO() {
    }

    public UpgradeProfileDTO(String email, String accountType, String firstName, String lastName, String companyName, String description, List<MultipartFile> profilePictures) {
        this.email = email;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.description = description;
        this.profilePictures = profilePictures;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<MultipartFile> getProfilePictures() {
        return profilePictures;
    }

    public void setProfilePictures(List<MultipartFile> profilePictures) {
        this.profilePictures = profilePictures;
    }
}
