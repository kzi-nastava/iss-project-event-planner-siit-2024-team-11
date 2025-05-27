package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventOrganizationPage {
    private WebDriver driver;
    private static final String PAGE_URL = "http://localhost:4200/organize-event/";

    @FindBy(css = "#continue-div:first-child")
    private WebElement continueButton;

    @FindBy(id = "name-input")
    private WebElement nameInput;

    @FindBy(id = "name-input-error")
    private WebElement nameInputError;

    @FindBy(id = "description-input")
    private WebElement descriptionInput;

    @FindBy(id = "description-input-error")
    private WebElement descriptionInputError;

    @FindBy(id = "maxParticipants-input")
    private WebElement maxParticipantsInput;

    @FindBy(id = "maxParticipants-input-required-error")
    private WebElement maxParticipantsInputRequiredError;

    @FindBy(id = "maxParticipants-input-positive-error")
    private WebElement maxParticipantsInputPositiveError;

    @FindBy(id = "privacy-public-input")
    private WebElement privacyPublicInput;

    @FindBy(id = "privacy-private-input")
    private WebElement privacyPrivateInput;

    @FindBy(id = "eventType-input")
    private WebElement eventTypeInput;

    @FindBy(id = "eventType-input-error")
    private WebElement eventTypeInputError;

    @FindBy(id = "map")
    private WebElement map;

    @FindBy(id = "no-location-text")
    private WebElement noLocationText;

    @FindBy(id = "date-input")
    private WebElement dateInput;

    @FindBy(id = "date-input-error")
    private WebElement dateInputError;

    @FindBy(id = "activity-name-input")
    private WebElement activityNameInput;

    @FindBy(id = "activity-name-input-error")
    private WebElement activityNameInputError;

    @FindBy(id = "activity-description-input")
    private WebElement activityDescriptionInput;

    @FindBy(id = "activity-description-input-error")
    private WebElement activityDescriptionInputError;

    @FindBy(id = "activity-location-input")
    private WebElement activityLocationInput;

    @FindBy(id = "activity-location-input-error")
    private WebElement activityLocationInputError;

    @FindBy(id = "activity-timeRange-input")
    private WebElement activityTimeRangeInput;

    @FindBy(id = "activity-timeRange-input-required-error")
    private WebElement activityTimeRangeInputRequiredError;

    @FindBy(id = "activity-timeRange-input-endTime-required-error")
    private WebElement activityTimeRangeInputEndTimeRequiredError;

    @FindBy(id = "activity-add-button")
    private WebElement activityAddButton;

    public EventOrganizationPage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public void pressContinueButton() {
        continueButton.click();
    }

    public void setEventName(String name) {
        nameInput.sendKeys(name);
    }

    public boolean hasNameError() {
        return nameInputError.isDisplayed();
    }

    public void setEventDescription(String description) {
        descriptionInput.sendKeys(description);
    }

    public boolean hasDescriptionError() {
        return descriptionInputError.isDisplayed();
    }

    public void setEventMaxParticipants(String maxParticipants) {
        maxParticipantsInput.sendKeys(maxParticipants);
    }

    public boolean hasMaxParticipantsRequiredError() {
        return maxParticipantsInputRequiredError.isDisplayed();
    }

    public boolean hasMaxParticipantsPositiveError() {
        return maxParticipantsInputPositiveError.isDisplayed();
    }

    public void setEventPrivacy(String privacy) {
        if (privacy.equals("Public")) {
            privacyPublicInput.click();
        } else {
            privacyPrivateInput.click();
        }
    }

    public void setEventType(String eventType) {
        eventTypeInput.click();
        driver.findElement(By.xpath("//mat-option/span[contains(text(),'" + eventType + "')]")).click();
    }

    public boolean hasEventTypeError() {
        return eventTypeInputError.isDisplayed();
    }

    public void setMapPin(double latitude, double longitude) {
        String script = "var map = document.getElementById('map')._leaflet_map;" +
                "var marker = L.marker([" + latitude + ", " + longitude + "]).addTo(map);" +
                "map.setView([" + latitude + ", " + longitude + "], 13);";
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
    }

    public boolean hasLocationError() {
        return noLocationText.isDisplayed();
    }

    public void setEventDate(String date) {
        dateInput.click();
        WebElement dateElement = driver.findElement(By.xpath("//div[contains(@class,'mat-calendar-body-cell')]//div[contains(text(),'" + date + "')]"));
        dateElement.click();
    }

    public boolean hasDateError() {
        return dateInputError.isDisplayed();
    }

    public void addActivity(String activityName, String activityDescription, String activityLocation, LocalDateTime activityStartTime, LocalDateTime activityEndTime) {
        activityNameInput.sendKeys(activityName);
        activityDescriptionInput.sendKeys(activityDescription);
        activityLocationInput.sendKeys(activityLocation);
        activityTimeRangeInput.click();
        activityTimeRangeInput.sendKeys(activityStartTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"))
                + " - " + activityEndTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")));

        activityAddButton.click();
    }

    public boolean hasActivityNameError() {
        return activityNameInputError.isDisplayed();
    }

    public boolean hasActivityDescriptionError() {
        return activityDescriptionInputError.isDisplayed();
    }

    public boolean hasActivityLocationError() {
        return activityLocationInputError.isDisplayed();
    }

    public boolean hasActivityTimeRangeError() {
        return activityTimeRangeInputRequiredError.isDisplayed();
    }

    public boolean hasActivityEndTimeRequiredError() {
        return activityTimeRangeInputEndTimeRequiredError.isDisplayed();
    }

}
