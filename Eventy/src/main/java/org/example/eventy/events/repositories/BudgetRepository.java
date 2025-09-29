package org.example.eventy.events.repositories;

import org.example.eventy.events.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByEventId(Long eventId);

    @Query("SELECT b FROM Budget b JOIN b.budgetedItems bi WHERE bi.id = :budgetItemId")
    Optional<Budget> findWhichContainsBudgetItemId(Long budgetItemId);
}
