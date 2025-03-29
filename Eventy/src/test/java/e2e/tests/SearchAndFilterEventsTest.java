package e2e.tests;

import e2e.base.ChromeTestBase;
import e2e.pages.HomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchAndFilterEventsTest extends ChromeTestBase {
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupTestData() {
        DataSource dataSource = new DriverManagerDataSource(
            "jdbc:postgresql://localhost:5432/eventytestdb", "postgres", "admin"
        );
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 4);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 5);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 6);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 7);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 8);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 9);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 10);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(5)), 11);
    }

    @Test
    public void filterEvents_EventTypesValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Workout");
        eventTypeNames.add("Party");
        eventTypeNames.add("EventType7");
        eventTypeNames.add("EventType8");
        home.selectEventTypes(eventTypeNames);

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 5 of 5");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 4");
        originalEventCardTitles.add("Event 7");
        originalEventCardTitles.add("Event 8");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_MaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("30");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 5 of 6");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 3");
        originalEventCardTitles.add("Event 4");
        originalEventCardTitles.add("Event 5");
        originalEventCardTitles.add("Event 6");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_MaxParticipantsValid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("1");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_MaxParticipantsInvalid_DoesNotFilter() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("0");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 5 of 9");
    }

    @Test
    public void filterEvents_EventTypesAndMaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Workout");
        home.selectEventTypes(eventTypeNames);

        home.enterMaxParticipants("20");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 3);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 3 of 3");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 4");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_EventTypesAndMaxParticipantsValid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Workout");
        home.selectEventTypes(eventTypeNames);

        home.enterMaxParticipants("1");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_LocationValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterLocation("Belgrade");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 3);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 3 of 3");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 3");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_LocationLowercaseValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterLocation("belgrade");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 3);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 3 of 3");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 3");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_LocationInvalid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterLocation("belgrad");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_LocationAndEventTypesValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Workout");
        home.selectEventTypes(eventTypeNames);

        home.enterLocation("belgrade");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 2 of 2");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_LocationAndMaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("35");

        home.enterLocation("gradiska");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 7");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_LocationValidAndMaxParticipantsInvalid_ReturnsEventsWithLocationOnly() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("0");

        home.enterLocation("belgrade");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 3);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 3 of 3");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 3");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_LocationAndEventTypesAndMaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Graduation");
        eventTypeNames.add("Workout");
        home.selectEventTypes(eventTypeNames);

        home.enterMaxParticipants("15");

        home.enterLocation("belgrade");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 3);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 3 of 3");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("Event 3");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 5 of 8");
    }

    @Test
    public void filterEvents_OnlyStartDate_ReturnsEvents() {
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(30)), 4);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(30)), 5);

        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(29);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 3);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 3 of 3");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");
        originalEventCardTitles.add("High School Graduation");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_OnlyEndTime_ReturnsEvents() {
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(1)), 4);
        jdbcTemplate.update("UPDATE events SET date = ? WHERE id = ?",
            Timestamp.valueOf(LocalDateTime.now().plusDays(1)), 5);

        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(2);
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));

        home.clickFilterButton();
        slowDown(); slowDown(); slowDown();slowDown(); slowDown(); slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 2 of 2");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateAndEventTypesValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Tech");
        eventTypeNames.add("Graduation");
        home.selectEventTypes(eventTypeNames);

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 2 of 2");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 3");
        originalEventCardTitles.add("Event 5");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateInvalidAndEventTypesValid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Tech");
        eventTypeNames.add("Graduation");
        home.selectEventTypes(eventTypeNames);

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(55);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(56);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_DateAndMaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("5");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateInvalidAndMaxParticipantsValid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("5");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(55);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(56);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_DateAndLocationValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterLocation("gradiska");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 2 of 2");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 7");
        originalEventCardTitles.add("Event 8");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateInvalidAndLocationValid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterLocation("gradiska");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(55);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(56);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_DateAndEventTypesAndMaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Tech");
        eventTypeNames.add("Graduation");
        home.selectEventTypes(eventTypeNames);

        home.enterMaxParticipants("20");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 3");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateInvalidAndEventTypesAndMaxParticipantsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Tech");
        eventTypeNames.add("Graduation");
        home.selectEventTypes(eventTypeNames);

        home.enterMaxParticipants("20");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(55);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(56);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_DateAndEventTypesAndLocationValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Tech");
        eventTypeNames.add("Graduation");
        home.selectEventTypes(eventTypeNames);

        home.enterLocation("nis");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 5");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateInvalidAndEventTypesAndLocationValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Tech");
        eventTypeNames.add("Graduation");
        home.selectEventTypes(eventTypeNames);

        home.enterLocation("nis");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(55);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(56);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_DateAndMaxParticipantsAndLocationValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("35");

        home.enterLocation("gradiska");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 7");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_DateInvalidAndMaxParticipantsAndLocationValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterMaxParticipants("35");

        home.enterLocation("gradiska");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(55);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(56);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_AllFilterInputsValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<String> eventTypeNames = new ArrayList<>();
        eventTypeNames.add("Workout");
        home.selectEventTypes(eventTypeNames);

        home.enterMaxParticipants("15");

        home.enterLocation("Belgrade");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        home.selectEndDate(dateTimeEnd.format(formatterEnd));
        home.selectStartDate(dateTimeStart.format(formatterStart));

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 2 of 2");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByNameValid_ReturnsEvent_Example1() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("event 1");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByNameValid_ReturnsEvent_Example2() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("1");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByNameInvalid_ReturnsEmptyList_Example1() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("event 11");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_SearchByNameInvalid_ReturnsEmptyList_Example2() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("11");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_SearchByNameValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("event");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 5 of 8");
    }

    @Test
    public void filterEvents_SearchByNameInvalid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("eventt");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_SearchByDescriptionValid_ReturnsEvent_Example1() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("D1");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByDescriptionValid_ReturnsEvent_Example2() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("d1");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByDescriptionValid_ReturnsEvent_Example3() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("jonny");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("High School Graduation");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByDescriptionValid_ReturnsEvent_Example4() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("duating");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 1);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 1 of 1");

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("High School Graduation");

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    @Test
    public void filterEvents_SearchByDescriptionInvalid_ReturnsEmptyList_Example1() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("D11");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_SearchByDescriptionInvalid_ReturnsEmptyList_Example2() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("d11");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_SearchByDescriptionInvalid_ReturnsEmptyList_Example3() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("jonnie");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    @Test
    public void filterEvents_SearchByDescriptionValid_ReturnsEvents() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("d");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "1 – 5 of 9");
    }

    @Test
    public void filterEvents_SearchByDescriptionInvalid_ReturnsEmptyList() {
        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        home.enterSearchQuery("dd");

        home.clickFilterButton();
        slowDown();

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 0);

        assertEquals(driver.findElement(By.cssSelector(".mat-mdc-paginator-range-label")).getText().strip(), "0 of 0");
    }

    // use this method only to slow down the automated test view, so you can see what is going on and what values are selected!
    // it's forbidden to use it for waiting for the element to show or anything similar related to the actual testing!
    public void slowDown() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
