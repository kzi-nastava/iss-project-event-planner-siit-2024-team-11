package org.example.eventy.events.repositories;

import org.example.eventy.events.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByEventId(Long eventId);
}
