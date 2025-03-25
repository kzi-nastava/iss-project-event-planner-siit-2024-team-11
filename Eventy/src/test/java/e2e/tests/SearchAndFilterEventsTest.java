package e2e.tests;

import e2e.base.ChromeTestBase;
import e2e.pages.HomePage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchAndFilterEventsTest extends ChromeTestBase {
    /*@Autowired
    private EventService eventService;

    @BeforeAll
    static void setupTestData() throws Exception {
        Event event1 = eventService.getEvent(4L);
        event1.setDate(LocalDateTime.now().plusDays(5));
        eventService.save(event1);


        Event event2 = eventService.getEvent(5L);
        event2.setDate(LocalDateTime.now().plusDays(5));
        eventService.save(event2);

        Event event3 = eventService.getEvent(6L);
        event3.setDate(LocalDateTime.now().plusDays(5));
        eventService.save(event3);

        Event event4 = eventService.getEvent(7L);
        event4.setDate(LocalDateTime.now().plusDays(5));
        eventService.save(event4);

        Event event5 = eventService.getEvent(8L);
        event5.setDate(LocalDateTime.now().plusDays(5));
        eventService.save(event5);
    }*/

    @Test
    public void filterEvents_ValidInput_ReturnsEvents() throws Exception {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        HomePage home = new HomePage(driver);
        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 5);
        /*

        //home.scrollToEvents();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(3);
        home.selectEventTypes(positions);

        home.enterMaxParticipants("15");

        home.enterLocation("Belgrade");

        LocalDateTime dateTimeStart = LocalDateTime.now().plusDays(3);
        LocalDateTime dateTimeEnd = LocalDateTime.now().plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        home.selectStartDate(dateTimeStart.format(formatter));
        home.selectEndDate(dateTimeEnd.format(formatter));

        home.clickFilterButton();
        home.scrollToEvents();

        List<String> originalEventCardTitles = new ArrayList<>();
        originalEventCardTitles.add("Event 1");
        originalEventCardTitles.add("Event 2");

        List<WebElement> eventCards = home.getFilteredEvents();
        assertEquals(eventCards.size(), 2);

        List<WebElement> eventCardTitles = home.getFilteredEventTitles();
        for (int i = 0; i < eventCardTitles.size(); i++) {
            assertEquals(eventCardTitles.get(i).getText(), originalEventCardTitles.get(i));
        }*/
    }
}
