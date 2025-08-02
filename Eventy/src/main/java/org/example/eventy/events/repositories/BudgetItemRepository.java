package org.example.eventy.events.repositories;

import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.solutions.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetItemRepository extends JpaRepository<BudgetItem, Long> {

    int removeById(Long id);
}
