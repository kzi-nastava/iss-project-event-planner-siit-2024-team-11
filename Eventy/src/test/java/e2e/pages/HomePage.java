package e2e.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private WebDriver driver;
    private static final String PAGE_URL = "http://localhost:4200/";

    @FindBy(id = "reset_event_filters_button")
    private WebElement resetAllButton;

    @FindBy(id = "filter_events_button")
    private WebElement filterButton;

    @FindBy(id = "search_events_button")
    private WebElement searchButton;

    @FindBy(css = "input.search_form")
    private WebElement searchInput;

    @FindBy(id = "event_types_filter_select")
    private WebElement eventTypesSelect;

    @FindBy(id = "max_participants_filter_select")
    private WebElement maxParticipantsInput;

    @FindBy(id = "location_filter_select")
    private WebElement locationInput;

    @FindBy(css = "input[formcontrolname='start']")
    private WebElement startDatePicker;
    @FindBy(css = "input[formcontrolname='end']")
    private WebElement endDatePicker;

    @FindBy(css = ".featured_card:first-child #title")
    private WebElement firstFeaturedEventTitle;

    @FindBy(css = ".featured_card:first-child #type")
    private WebElement firstFeaturedEventType;

    @FindBy(css = ".featured_card:first-child #max_participants")
    private WebElement firstFeaturedEventMaxParticipants;

    @FindBy(css = ".featured_card:first-child #date")
    private WebElement firstFeaturedEventDate;

    @FindBy(css = ".featured_card:first-child #location")
    private WebElement firstFeaturedEventLocation;

    @FindBy(css = ".featured_card:first-child #description")
    private WebElement firstFeaturedEventDescription;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public void enterSearchQuery(String query) {
        searchInput.clear();
        searchInput.sendKeys(query);
    }

    public void selectEventTypes(ArrayList<String> eventTypeNames) {
        (new WebDriverWait(driver, Duration.ofSeconds(1)))
            .until(ExpectedConditions.elementToBeClickable(eventTypesSelect)).click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        List<WebElement> eventTypeMatOptions = driver.findElements(By.cssSelector("mat-option"));
        for (String eventType : eventTypeNames) {
            for (WebElement option : eventTypeMatOptions) {
                if (option.getText().equals(eventType)) {
                    option.click();
                }
            }
        }

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).perform();
    }

    public void enterMaxParticipants(String maxParticipants) {
        maxParticipantsInput.clear();
        maxParticipantsInput.sendKeys(maxParticipants);
    }

    public void enterLocation(String location) {
        locationInput.clear();
        locationInput.sendKeys(location);
    }

    public void selectStartDate(String startDate) {
        startDatePicker.sendKeys(startDate);
    }

    public void selectEndDate(String endDate) {
        endDatePicker.sendKeys(endDate);
    }

    public void clickResetAllButton() {
        (new WebDriverWait(driver, Duration.ofSeconds(1)))
            .until(ExpectedConditions.elementToBeClickable(resetAllButton)).click();
    }

    public void clickFilterButton() {
        (new WebDriverWait(driver, Duration.ofSeconds(1)))
            .until(ExpectedConditions.elementToBeClickable(filterButton)).click();
    }

    public void clickSearchButton() {
        (new WebDriverWait(driver, Duration.ofSeconds(1)))
            .until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public List<WebElement> getFilteredEvents() {
        return driver.findElements(By.cssSelector("#all_event_cards_home app-event-card"));
    }

    public List<WebElement> getFilteredEventTitles() {
        return driver.findElements(By.cssSelector(".card_container > .title"));
    }

    public List<WebElement> getEventCardDescriptions() {
        return driver.findElements(By.cssSelector(".card_container > #description"));
    }

    public List<WebElement> getEventCardEventTypes() {
        return driver.findElements(By.cssSelector(".card_container > #type"));
    }

    public List<WebElement> getEventCardMaxParticipants() {
        return driver.findElements(By.cssSelector(".card_container > #max_participants"));
    }

    public List<WebElement> getEventCardLocations() {
        return driver.findElements(By.cssSelector(".card_container > #location"));
    }

    public List<WebElement> getEventCardDates() {
        return driver.findElements(By.cssSelector(".card_container > #date"));
    }

    public void scrollToEvents() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement eventsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@style='background-color: #F1F1F1; width: 100%;']")));
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'start'});", eventsContainer);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#event_filters")));
    }

    public String getFirstFeaturedEventTitle() {
        return firstFeaturedEventTitle.getText();
    }

    public String getFirstFeaturedEventType() {
        return firstFeaturedEventType.getText();
    }
    public String getFirstFeaturedEventMaxParticipants() {
        return firstFeaturedEventMaxParticipants.getText();
    }
    public String getFirstFeaturedEventDate() {
        return firstFeaturedEventDate.getText();
    }

    public String getFirstFeaturedEventDescription() {
        return firstFeaturedEventDescription.getText();
    }
}
