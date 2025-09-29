package repository;

import jakarta.validation.ConstraintViolationException;
import org.example.eventy.EventyApplication;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.events.repositories.BudgetItemRepository;
import org.example.eventy.solutions.repositories.SolutionCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = EventyApplication.class)
@ActiveProfiles("test")
@Transactional
public class BudgetItemRepositoryTest {
    @Autowired
    private BudgetItemRepository budgetItemRepository;

    @Autowired
    private SolutionCategoryRepository solutionCategoryRepository;

    @Test
    void saveBudgetItem_AllValid_ReturnsBudgetItem() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setCategory(solutionCategoryRepository.findById(1L).get());
        budgetItem.setPlannedFunds(10000.0);
        budgetItem.setReservedItems(new ArrayList<>());
        BudgetItem repoBudgetItem = budgetItemRepository.save(budgetItem);
        assertThat(budgetItem).usingRecursiveComparison().isEqualTo(repoBudgetItem);
    }

    @Test
    void saveBudgetItem_PlannedFundsInvalid_ThrowsException() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setCategory(solutionCategoryRepository.findById(1L).get());
        budgetItem.setPlannedFunds(-10000.0);
        budgetItem.setReservedItems(new ArrayList<>());
        assertThrows(ConstraintViolationException.class, () -> {
            budgetItemRepository.save(budgetItem);
        });
    }

    @Test
    void saveBudgetItem_CategoryNull_ThrowsException() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setCategory(null);
        budgetItem.setPlannedFunds(10000.0);
        budgetItem.setReservedItems(new ArrayList<>());
        assertThrows(DataIntegrityViolationException.class, () -> {
            budgetItemRepository.save(budgetItem);
        });
    }

    @Test
    void removeBudgetItemById_ValidId_DeletesBudgetItem() {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setCategory(solutionCategoryRepository.findById(1L).get());
        budgetItem.setPlannedFunds(10000.0);
        budgetItem.setReservedItems(new ArrayList<>());
        BudgetItem repoBudgetItem = budgetItemRepository.save(budgetItem);
        budgetItemRepository.removeById(repoBudgetItem.getId());
        repoBudgetItem = budgetItemRepository.findById(repoBudgetItem.getId()).orElse(null);
        assertNull(repoBudgetItem);
    }
}
