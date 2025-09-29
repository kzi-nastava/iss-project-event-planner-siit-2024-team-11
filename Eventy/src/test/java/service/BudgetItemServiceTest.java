package service;

import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.repositories.BudgetItemRepository;
import org.example.eventy.events.services.BudgetItemService;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.SolutionHistory;
import org.example.eventy.solutions.services.SolutionCategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = EventyApplication.class)
@ActiveProfiles("test")
public class BudgetItemServiceTest {
    @Mock
    private BudgetItemRepository budgetItemRepository;
    @InjectMocks
    private BudgetItemService budgetItemService;

    @Test
    void createBudgetItem_ValidInput_ReturnsBudgetItem() {
        BudgetItem mockBudgetItem = new BudgetItem();

        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenReturn(mockBudgetItem);

        BudgetItem savedBudgetItem = budgetItemService.createBudgetItem(new Category(), 0.0);

        assertNotNull(savedBudgetItem);
        assertEquals(mockBudgetItem, savedBudgetItem);

        verify(budgetItemRepository, times(1)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void createBudgetItem_InvalidFunds_ThrowsException() {
        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenThrow(new RuntimeException("Database error"));

        BudgetItem savedBudgetItem = budgetItemService.createBudgetItem(new Category(), -1.0);

        assertNull(savedBudgetItem);

        verify(budgetItemRepository, times(1)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void updateBudgetItemFunds_InvalidFunds_ReturnsNull() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));

        BudgetItem savedBudgetItem = budgetItemService.updateAllocatedFunds(1L, -1.0);

        assertNull(savedBudgetItem);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(0)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void updateBudgetItemFunds_BudgetItemNotFound_ReturnsNull() {
        when(budgetItemRepository.findById(2L)).thenReturn(Optional.empty());

        BudgetItem savedBudgetItem = budgetItemService.updateAllocatedFunds(2L, 200.0);

        assertNull(savedBudgetItem);

        verify(budgetItemRepository, times(1)).findById(2L);
        verify(budgetItemRepository, times(0)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void updateBudgetItemFunds_EverythingValid_ReturnsBudgetItem() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);

        BudgetItem budgetItem2 = new BudgetItem();
        budgetItem2.setId(1L);
        budgetItem2.setCategory(null);
        budgetItem2.setPlannedFunds(200.0);

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));
        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenReturn(budgetItem2);

        BudgetItem savedBudgetItem = budgetItemService.updateAllocatedFunds(1L, 200.0);

        assertNotNull(savedBudgetItem);
        assertEquals(budgetItem2, savedBudgetItem);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(1)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void addBudgetItemSolution_EverythingValid_ReturnsBudgetItem() {
        SolutionHistory solutionHistory = new SolutionHistory();
        solutionHistory.setId(1L);
        solutionHistory.setName("test");
        solutionHistory.setDescription("test");
        solutionHistory.setDiscount(0);
        solutionHistory.setPrice(100.0);
        solutionHistory.setCancellationDeadline(null);
        solutionHistory.setProviderId(1L);

        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);
        budgetItem.setReservedItems(new ArrayList<>());

        BudgetItem budgetItem2 = new BudgetItem();
        budgetItem2.setId(1L);
        budgetItem2.setCategory(null);
        budgetItem2.setPlannedFunds(100.0);
        budgetItem2.setReservedItems(List.of(solutionHistory));

        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenReturn(budgetItem2);

        BudgetItem savedBudgetItem = budgetItemService.addBudgetItemSolution(budgetItem, solutionHistory);

        assertNotNull(savedBudgetItem);
        assertEquals(budgetItem2, savedBudgetItem);
    }

    @Test
    void removeBudgetItemSolution_EverythingValid_ReturnsTrue() {
        SolutionHistory solutionHistory = new SolutionHistory();
        solutionHistory.setId(1L);
        solutionHistory.setName("test");
        solutionHistory.setDescription("test");
        solutionHistory.setDiscount(0);
        solutionHistory.setPrice(100.0);
        solutionHistory.setCancellationDeadline(null);
        solutionHistory.setProviderId(1L);

        List<SolutionHistory> list = new ArrayList<>();
        list.add(solutionHistory);
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);
        budgetItem.setReservedItems(list);

        BudgetItem budgetItem2 = new BudgetItem();
        budgetItem2.setId(1L);
        budgetItem2.setCategory(null);
        budgetItem2.setPlannedFunds(100.0);
        budgetItem2.setReservedItems(new ArrayList<>());

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));
        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenReturn(budgetItem2);

        boolean success = budgetItemService.deleteBudgetItemSolution(1L, solutionHistory.getId(), LocalDateTime.now().plusDays(10));

        assertTrue(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(1)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void removeBudgetItemSolution_BudgetItemNotFound_ReturnsFalse() {
        when(budgetItemRepository.findById(1L)).thenReturn(Optional.empty());

        boolean success = budgetItemService.deleteBudgetItemSolution(1L, 1L, LocalDateTime.now().plusDays(10));

        assertFalse(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(0)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void removeBudgetItemSolution_BudgetItemSolutionNotFound_ReturnsFalse() {
        SolutionHistory solutionHistory = new SolutionHistory();
        solutionHistory.setId(1L);
        solutionHistory.setName("test");
        solutionHistory.setDescription("test");
        solutionHistory.setDiscount(0);
        solutionHistory.setPrice(100.0);
        solutionHistory.setCancellationDeadline(null);
        solutionHistory.setProviderId(1L);

        List<SolutionHistory> list = new ArrayList<>();
        list.add(solutionHistory);
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);
        budgetItem.setReservedItems(list);

        BudgetItem budgetItem2 = new BudgetItem();
        budgetItem2.setId(1L);
        budgetItem2.setCategory(null);
        budgetItem2.setPlannedFunds(100.0);
        budgetItem2.setReservedItems(list);

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));
        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenReturn(budgetItem2);

        boolean success = budgetItemService.deleteBudgetItemSolution(1L, 2L, LocalDateTime.now().plusDays(10));

        assertFalse(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(1)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void removeBudgetItemSolution_CancellationDeadlineTooClose_ReturnsFalse() {
        SolutionHistory solutionHistory = new SolutionHistory();
        solutionHistory.setId(1L);
        solutionHistory.setName("test");
        solutionHistory.setDescription("test");
        solutionHistory.setDiscount(0);
        solutionHistory.setPrice(100.0);
        solutionHistory.setCancellationDeadline(100);
        solutionHistory.setProviderId(1L);

        List<SolutionHistory> list = new ArrayList<>();
        list.add(solutionHistory);
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);
        budgetItem.setReservedItems(list);

        BudgetItem budgetItem2 = new BudgetItem();
        budgetItem2.setId(1L);
        budgetItem2.setCategory(null);
        budgetItem2.setPlannedFunds(100.0);
        budgetItem2.setReservedItems(new ArrayList<>());

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));
        when(budgetItemRepository.save(Mockito.any(BudgetItem.class))).thenReturn(budgetItem2);

        boolean success = budgetItemService.deleteBudgetItemSolution(1L, solutionHistory.getId(), LocalDateTime.now().plusDays(5));

        assertFalse(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(1)).save(Mockito.any(BudgetItem.class));
    }

    @Test
    void removeBudgetItem_allValid_ReturnsTrue() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);
        budgetItem.setReservedItems(new ArrayList<>());

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));

        boolean success = budgetItemService.deleteBudgetItem(budgetItem.getId());

        assertTrue(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void removeBudgetItem_remainingBudgetItemSolutions_ReturnsFalse() {
        SolutionHistory solutionHistory = new SolutionHistory();
        solutionHistory.setId(1L);
        solutionHistory.setName("test");
        solutionHistory.setDescription("test");
        solutionHistory.setDiscount(0);
        solutionHistory.setPrice(100.0);
        solutionHistory.setCancellationDeadline(null);
        solutionHistory.setProviderId(1L);
        List<SolutionHistory> list = new ArrayList<>();
        list.add(solutionHistory);

        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setId(1L);
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(100.0);
        budgetItem.setReservedItems(list);

        when(budgetItemRepository.findById(1L)).thenReturn(Optional.of(budgetItem));

        boolean success = budgetItemService.deleteBudgetItem(budgetItem.getId());

        assertFalse(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(0)).deleteById(1L);
    }

    @Test
    void removeBudgetItem_BudgetItemNotFound_ReturnsFalse() {
        when(budgetItemRepository.findById(1L)).thenReturn(Optional.empty());

        boolean success = budgetItemService.deleteBudgetItem(1L);

        assertFalse(success);

        verify(budgetItemRepository, times(1)).findById(1L);
        verify(budgetItemRepository, times(0)).deleteById(1L);
    }
}
