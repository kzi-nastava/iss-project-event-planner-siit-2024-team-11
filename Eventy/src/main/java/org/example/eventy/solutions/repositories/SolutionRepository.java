package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    public List<Solution> findAllByProviderId(Long providerId);
}
