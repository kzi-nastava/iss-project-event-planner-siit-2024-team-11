package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".cdk-overlay-backdrop")
        ));

        wait.until(ExpectedConditions.elementToBeClickable(continueButton));

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
        maxParticipantsInput.clear();
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        By optionLocator = By.xpath("//mat-option/span[contains(text(),'" + eventType + "')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));

        driver.findElement(optionLocator).click();
    }

    public boolean hasEventTypeError() {
        return eventTypeInputError.isDisplayed();
    }

    public void setMapPin() {
        map.click();
    }

    public boolean hasLocationError() {
        return noLocationText.isDisplayed();
    }

    public void setEventDate(LocalDate targetDate) {
        WebElement calendarToggle = driver.findElement(By.cssSelector("mat-datepicker-toggle button"));
        calendarToggle.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement periodButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".mat-calendar-period-button")
        ));
        periodButton.click();

        By yearLocator = By.xpath("//span[contains(@class,'mat-calendar-body-cell-content') and text()=' " + targetDate.getYear() + " ']");
        WebElement year = wait.until(ExpectedConditions.elementToBeClickable(yearLocator));
        year.click();

        String month = targetDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        By monthLocator = By.xpath("//span[contains(@class,'mat-calendar-body-cell-content') and text()=' " + month.toUpperCase() + " ']");
        WebElement monthEl = wait.until(ExpectedConditions.elementToBeClickable(monthLocator));
        monthEl.click();

        String day = String.valueOf(targetDate.getDayOfMonth());
        By dayLocator = By.xpath("//span[contains(@class,'mat-calendar-body-cell-content') and text()=' " + day + " ']");
        WebElement dayEl = wait.until(ExpectedConditions.elementToBeClickable(dayLocator));
        dayEl.click();
    }

    public boolean hasDateError() {
        return dateInputError.isDisplayed();
    }

    private void setTime(WebDriverWait wait, LocalDateTime time) {
        List<WebElement> timerInputs = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector(".owl-dt-timer-input"), 1
        ));

        WebElement hourInput = timerInputs.get(0);
        WebElement minuteInput = timerInputs.get(1);

        hourInput.clear();
        hourInput.sendKeys(String.format("%02d", time.getHour()));

        minuteInput.clear();
        minuteInput.sendKeys(String.format("%02d", time.getMinute()));
    }

    public void addActivity(String activityName, String activityDescription, String activityLocation, LocalDateTime activityStartTime, LocalDateTime activityEndTime) {
        activityNameInput.sendKeys(activityName);
        activityDescriptionInput.sendKeys(activityDescription);
        activityLocationInput.sendKeys(activityLocation);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        activityTimeRangeInput.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("owl-date-time-container")));

        WebElement startDayEl = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'owl-dt-calendar-cell-content') and text()=' " + activityStartTime.getDayOfMonth() + " ']")
        ));
        startDayEl.click();

        setTime(wait, activityStartTime);

        WebElement endDayEl = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(@class,'owl-dt-calendar-cell-content') and text()=' " + activityEndTime.getDayOfMonth() + " ']")
        ));
        endDayEl.click();

        setTime(wait, activityEndTime);

        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'owl-dt-control-button')]//span[text()=' Set ']")
        ));

        confirmBtn.click();

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
