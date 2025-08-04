package e2e.tests;

import e2e.base.ChromeTestBase;
import e2e.pages.EventOrganizationPage;
import e2e.pages.HomePage;
import e2e.pages.LoginPage;
import org.example.eventy.EventyApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = EventyApplication.class)
@ActiveProfiles("test") // if you use application-test.properties
public class EventManagementTest extends ChromeTestBase {
    private LoginPage loginPage;
    private EventOrganizationPage eventOrganizationPage;

    @BeforeEach
    public void setupTest() {
        loginPage = new LoginPage(driver);
        loginPage.login("tac@gmail.com", "admin");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/"));

        eventOrganizationPage = new EventOrganizationPage(driver);
    }

    @AfterEach
    public void tearDownTest() {
        loginPage.logout();
    }
    
    @Test
    public void organizeEvent_AllValid_CreatesEvent() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "This is a sample activity description.", "Activity Location", LocalDateTime.of(2025, 10, 7, 10, 0, 0), LocalDateTime.of(2025, 10, 7, 12, 0, 0));

        eventOrganizationPage.pressContinueButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".featured_card")));

        HomePage homePage = new HomePage(driver);
        assertEquals("Sample Event", homePage.getFirstFeaturedEventTitle());
        assertEquals("This is a sample event description.", homePage.getFirstFeaturedEventDescription());
        assertEquals("100", homePage.getFirstFeaturedEventMaxParticipants());
        assertEquals("Party", homePage.getFirstFeaturedEventType());
        assertEquals("October 7, 2025", homePage.getFirstFeaturedEventDate());
    }

    @Test
    public void organizeEvent_NameNotSet_RaisesAnError() {
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasNameError());
    }

    @Test
    public void organizeEvent_NameEmpty_RaisesAnError() {
        eventOrganizationPage.setEventName("");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasNameError());
    }

    @Test
    public void organizeEvent_DescriptionNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasDescriptionError());
    }

    @Test
    public void organizeEvent_DescriptionEmpty_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasDescriptionError());
    }

    @Test
    public void organizeEvent_MaxParticipantsNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasMaxParticipantsPositiveError());
    }

    @Test
    public void organizeEvent_MaxParticipantsEmpty_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasMaxParticipantsPositiveError());
    }

    @Test
    public void organizeEvent_MaxParticipantsNotNumbers_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("Not a number");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasMaxParticipantsPositiveError());
    }

    @Test
    public void organizeEvent_MaxParticipantsNegative_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("-1");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasMaxParticipantsPositiveError());
    }

    @Test
    public void organizeEvent_MaxParticipantsZero_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("0");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasMaxParticipantsPositiveError());
    }

    @Test
    public void organizeEvent_MaxParticipantsDecimal_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("12,34");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasMaxParticipantsPositiveError());
    }

    @Test
    public void organizeEvent_NoMapPin_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        assertTrue(eventOrganizationPage.hasLocationError());
    }

    @Test
    public void organizeEvent_EventTypeNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasEventTypeError());
    }

    @Test
    public void organizeEvent_EventDateNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.hasDateError());
    }

    @Test
    public void organizeEvent_AgendaEmpty_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.pressContinueButton();

        assertTrue(eventOrganizationPage.isErrorDialogDisplayed());

        eventOrganizationPage.closeErrorDialog();
    }

    @Test
    public void organizeEvent_ActivityNameNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("", "This is a sample activity description.", "Activity Location", LocalDateTime.of(2025, 10, 7, 10, 0, 0), LocalDateTime.of(2025, 10, 7, 12, 0, 0));

        assertTrue(eventOrganizationPage.hasActivityNameError());
    }

    @Test
    public void organizeEvent_ActivityDescriptionNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "", "Activity Location", LocalDateTime.of(2025, 10, 7, 10, 0, 0), LocalDateTime.of(2025, 10, 7, 12, 0, 0));

        assertTrue(eventOrganizationPage.hasActivityDescriptionError());
    }

    @Test
    public void organizeEvent_ActivityLocationNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "This is a sample activity description.", "", LocalDateTime.of(2025, 10, 7, 10, 0, 0), LocalDateTime.of(2025, 10, 7, 12, 0, 0));

        assertTrue(eventOrganizationPage.hasActivityLocationError());
    }

    @Test
    public void organizeEvent_ActivityTimeRangeNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "This is a sample activity description.", "Activity Location", null, null);

        assertTrue(eventOrganizationPage.hasActivityTimeRangeError());
    }

    @Test
    public void organizeEvent_ActivityEndTimeNotSet_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "This is a sample activity description.", "Activity Location", LocalDateTime.of(2025, 10, 7, 10, 0, 0), null);

        assertTrue(eventOrganizationPage.hasActivityEndTimeRequiredError());
    }

    @Test
    public void organizeEvent_ActivityTimeRangeZero_RaisesAnError() {
        eventOrganizationPage.setEventName("Sample Event");
        eventOrganizationPage.setEventDescription("This is a sample event description.");
        eventOrganizationPage.setEventMaxParticipants("100");
        eventOrganizationPage.setEventPrivacy("Public");
        eventOrganizationPage.setEventType("Party");
        eventOrganizationPage.setMapPin();
        eventOrganizationPage.setEventDate(LocalDate.of(2025, 10, 7));

        eventOrganizationPage.pressContinueButton();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#activity-name-input")));

        eventOrganizationPage.addActivity("Activity 1", "This is a sample activity description.", "Activity Location", LocalDateTime.of(2025, 10, 7, 10, 0, 0), LocalDateTime.of(2025, 10, 7, 10, 0, 0));

        assertTrue(eventOrganizationPage.hasActivityEndTimeRequiredError());
    }
}
