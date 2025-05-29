package e2e.tests;

import e2e.base.ChromeTestBase;
import e2e.pages.EventOrganizationPage;
import e2e.pages.HomePage;
import e2e.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventManagementTest extends ChromeTestBase {
    private LoginPage loginPage;
    private EventOrganizationPage eventOrganizationPage;

    @BeforeEach
    public void setupTest() {
        loginPage = new LoginPage(driver);
        loginPage.login("ves@gmail.com", "admin");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/"));

        eventOrganizationPage = new EventOrganizationPage(driver);
    }

    @AfterEach
    public void tearDownTest() {
        loginPage.logout();
    }
    
    @Test
    public void organizeEvent_allValid_createsEvent() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 7, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "This is a sample activity description.", "Activity Location", LocalDateTime.of(2025, 7, 7, 10, 0, 0), LocalDateTime.of(2025, 7, 7, 12, 0, 0));

        eventOrganizationPage.pressContinueButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".featured_card")));

        HomePage homePage = new HomePage(driver);
        assertEquals("Sample Event", homePage.getFirstFeaturedEventTitle());
        assertEquals("This is a sample event description.", homePage.getFirstFeaturedEventDescription());
        assertEquals("100", homePage.getFirstFeaturedEventMaxParticipants());
        assertEquals("Party", homePage.getFirstFeaturedEventType());
        assertEquals("July 7, 2025", homePage.getFirstFeaturedEventDate());
    }
}
