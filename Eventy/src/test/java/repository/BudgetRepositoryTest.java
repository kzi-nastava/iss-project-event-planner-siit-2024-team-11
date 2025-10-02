package repository;

import org.example.eventy.EventyApplication;
import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.*;
import org.example.eventy.events.repositories.BudgetRepository;
import org.example.eventy.events.repositories.EventRepository;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.SolutionHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EventyApplication.class)
@ActiveProfiles("test")
@Transactional
public class BudgetRepositoryTest {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void getBudgetByEventId_Exists_ReturnsBudget() {
        Category category1 = new Category(1L, "Food", "Catering and refreshments", Status.ACCEPTED);
        Category category2 = new Category(2L, "Venue", "Venue rental and setup", Status.ACCEPTED);

        SolutionHistory solution1 = new SolutionHistory(1L, 101L, "Catering Service", "Delicious catering for the event", 200.0, 10, 48);
        SolutionHistory solution2 = new SolutionHistory(2L, 102L, "Venue Booking", "Venue reservation for the event", 500.0, 15, 72);

        BudgetItem item1 = new BudgetItem(category1, 250.0);
        item1.setReservedItems(List.of(solution1));

        BudgetItem item2 = new BudgetItem(category2, 600.0);
        item2.setReservedItems(List.of(solution2));

        List<BudgetItem> budgetItems = new ArrayList<>();
        budgetItems.add(item1);
        budgetItems.add(item2);

        Budget budget = new Budget();
        budget.setBudgetedItems(budgetItems);
        budget.setEvent(eventRepository.getReferenceById(11L));

        budgetRepository.save(budget);

        Budget repoBudget = budgetRepository.findByEventId(11L).orElse(null);

        assertThat(budget).usingRecursiveComparison().isEqualTo(repoBudget);
    }

    @Test
    void getBudgetByEventId_NotExists_ReturnsNull() {
        Category category1 = new Category(1L, "Food", "Catering and refreshments", Status.ACCEPTED);
        Category category2 = new Category(2L, "Venue", "Venue rental and setup", Status.ACCEPTED);

        SolutionHistory solution1 = new SolutionHistory(1L, 101L, "Catering Service", "Delicious catering for the event", 200.0, 10, 48);
        SolutionHistory solution2 = new SolutionHistory(2L, 102L, "Venue Booking", "Venue reservation for the event", 500.0, 15, 72);

        BudgetItem item1 = new BudgetItem(category1, 250.0);
        item1.setReservedItems(List.of(solution1));

        BudgetItem item2 = new BudgetItem(category2, 600.0);
        item2.setReservedItems(List.of(solution2));

        List<BudgetItem> budgetItems = new ArrayList<>();
        budgetItems.add(item1);
        budgetItems.add(item2);

        Budget budget = new Budget();
        budget.setBudgetedItems(budgetItems);
        budget.setEvent(eventRepository.getReferenceById(11L));

        budgetRepository.save(budget);

        Budget repoBudget = budgetRepository.findByEventId(12L).orElse(null);

        assertNull(repoBudget);
    }

    @Test
    void saveBudget_EventNotGiven_ThrowsException() {
        Category category1 = new Category(1L, "Food", "Catering and refreshments", Status.ACCEPTED);
        Category category2 = new Category(2L, "Venue", "Venue rental and setup", Status.ACCEPTED);

        SolutionHistory solution1 = new SolutionHistory(1L, 101L, "Catering Service", "Delicious catering for the event", 200.0, 10, 48);
        SolutionHistory solution2 = new SolutionHistory(2L, 102L, "Venue Booking", "Venue reservation for the event", 500.0, 15, 72);

        BudgetItem item1 = new BudgetItem(category1, 250.0);
        item1.setReservedItems(List.of(solution1));

        BudgetItem item2 = new BudgetItem(category2, 600.0);
        item2.setReservedItems(List.of(solution2));

        List<BudgetItem> budgetItems = new ArrayList<>();
        budgetItems.add(item1);
        budgetItems.add(item2);

        Budget budget = new Budget();
        budget.setBudgetedItems(budgetItems);
        budget.setEvent(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            budgetRepository.save(budget);
        });
    }
}
