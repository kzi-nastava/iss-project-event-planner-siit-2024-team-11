package e2e.pages;

import org.hibernate.annotations.processing.Find;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ReservationPage {
    private WebDriver driver;

    @FindBy(css = "app-event-card")
    private List<WebElement> eventCards;

    @FindBy(id = "reservation-continue-button")
    private WebElement continueButton;

    @FindBy(id = "date-input-field")
    private WebElement eventDate;

    @FindBy(id = "start-time-input")
    private WebElement startTime;

    @FindBy(id = "end-time-input")
    private WebElement endTime;

    @FindBy(id = "confirm-reservation-button")
    private WebElement confirmReservationButton;

    @FindBy(css = ".yes_no_dialog")
    private WebElement yesNoDialog;

    @FindBy(id = "yes-no-dialog-confirm-button")
    private WebElement yesNoDialogConfirmButton;

    @FindBy(css = ".successful_dialog")
    private WebElement successfulDialog;

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); // Initialize @FindBy elements
    }

    public void clickEventCard() {
        eventCards.get(0).click();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void setEventDate(String date) {
        eventDate.sendKeys(date);
    }

    public void setStartTime(String time) {
        startTime.sendKeys(time);
    }

    public void setEndTime(String time) {
        endTime.sendKeys(time);
    }

    public void clickConfirmReservationButton() {
        confirmReservationButton.click();
    }

    public boolean isYesNoDialogVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOf(yesNoDialog));
            return yesNoDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickYesNoDialogConfirmButton() {
        yesNoDialogConfirmButton.click();
    }

    public boolean isSuccessfulDialogVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOf(successfulDialog));
            return successfulDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}

