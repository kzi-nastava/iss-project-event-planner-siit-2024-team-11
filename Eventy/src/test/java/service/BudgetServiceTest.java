package service;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.Budget;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.models.EventType;
import org.example.eventy.events.repositories.BudgetRepository;
import org.example.eventy.events.services.BudgetItemService;
import org.example.eventy.events.services.BudgetService;
import org.example.eventy.events.services.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = EventyApplication.class)
@ActiveProfiles("test")
public class BudgetServiceTest {
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private BudgetItemService budgetItemService;
    @Mock
    private EventService eventService;
    @InjectMocks
    private BudgetService budgetService;

    @Test
    void createBudget_AllValid_ReturnsBudget() {
        Budget budget = new Budget();
        Event event = new Event();
        event.setType(new EventType());
        event.getType().setRecommendedSolutionCategories(new HashSet<>());

        when(eventService.getEvent(1L)).thenReturn(event);
        when(budgetRepository.save(Mockito.any(Budget.class))).thenReturn(budget);

        Budget returnedBudget = budgetService.createBudget(1L);

        assertNotNull(returnedBudget);
        assertEquals(budget, returnedBudget);

        verify(budgetRepository, times(1)).save(Mockito.any(Budget.class));
        verify(eventService, times(1)).getEvent(1L);
    }

    @Test
    void createBudget_EventNotFound_ReturnsNulL() {
        when(eventService.getEvent(1L)).thenReturn(null);

        Budget returnedBudget = budgetService.createBudget(1L);

        assertNull(returnedBudget);
        verify(budgetRepository, times(0)).save(Mockito.any(Budget.class));
        verify(eventService, times(1)).getEvent(1L);
    }

    @Test
    void getBudgetByEventId_ValidID_ReturnsBudget() {
        Budget budget = new Budget();
        when(budgetRepository.findByEventId(1L)).thenReturn(Optional.of(budget));

        Budget actualBudget = budgetService.getBudget(1L);

        assertNotNull(actualBudget);
        assertEquals(budget, actualBudget);
        verify(budgetRepository, times(1)).findByEventId(1L);
    }

    @Test
    void getBudgetByEventId_InvalidID_ReturnsBudget() {
        when(budgetRepository.findByEventId(1L)).thenReturn(Optional.empty());

        Budget actualBudget = budgetService.getBudget(1L);

        assertNull(actualBudget);
        verify(budgetRepository, times(1)).findByEventId(1L);
    }

    @Test
    void addBudgetItem_AllValid_ReturnsBudget() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);

        List<BudgetItem> list = new ArrayList<>();
        list.add(budgetItem);

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setBudgetedItems(new ArrayList<>());

        Budget budget2 = new Budget();
        budget2.setId(1L);
        budget2.setBudgetedItems(list);

        when(budgetRepository.save(Mockito.any(Budget.class))).thenReturn(budget2);

        Budget returnedBudget = budgetService.addBudgetItem(budget, budgetItem);

        assertNotNull(returnedBudget);
        assertEquals(budget2, returnedBudget);
        verify(budgetRepository, times(1)).save(Mockito.any(Budget.class));
    }

    @Test
    void removeBudgetItem_AllValid_ReturnsTrue() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);

        List<BudgetItem> list = new ArrayList<>();
        list.add(budgetItem);

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setBudgetedItems(list);

        Budget budget2 = new Budget();
        budget2.setId(1L);
        budget2.setBudgetedItems(new ArrayList<>());

        when(budgetRepository.save(Mockito.any(Budget.class))).thenReturn(budget2);

        boolean success = budgetService.deleteBudgetItemFromBudget(budget, budgetItem.getId());

        assertTrue(success);
        verify(budgetRepository, times(1)).save(Mockito.any(Budget.class));
    }

    @Test
    void removeBudgetItem_BudgetItemNotFound_ReturnsFalse() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setBudgetedItems(new ArrayList<>());

        boolean success = budgetService.deleteBudgetItemFromBudget(budget, 1L);

        assertFalse(success);
        verify(budgetRepository, times(0)).save(Mockito.any(Budget.class));
    }
}
