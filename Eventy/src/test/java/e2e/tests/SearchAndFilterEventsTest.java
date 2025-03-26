package e2e.tests;

import e2e.base.ChromeTestBase;
import e2e.pages.HomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupTestData() throws Exception {
        dataSource = new DriverManagerDataSource(
            "jdbc:postgresql://localhost:5432/eventytestdb", "postgres", "admin"
        );
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void filterEvents_ValidInput_ReturnsEvents() throws Exception {
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

        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(3);
        home.selectEventTypes(positions);

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

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }

    /*@Test
    public void filterEvents_ValidInput_ReturnsEvents() throws Exception {
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

        HomePage home = new HomePage(driver);
        home.scrollToEvents();
        slowDown();

        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(3);
        home.selectEventTypes(positions);

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

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }
    }*/

    // use this method only to slow down the automated test view so you can see what is going on and what values are selected!
    // it's forbidden to use it for waiting for the element to show or anything similar related to the actual testing!
    public void slowDown() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
