package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.SolutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionHistoryRepository extends JpaRepository<SolutionHistory, Long> {
}
