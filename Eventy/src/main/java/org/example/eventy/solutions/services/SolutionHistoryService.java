package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.SolutionHistory;
import org.example.eventy.solutions.repositories.SolutionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolutionHistoryService {
    @Autowired
    private SolutionHistoryRepository solutionHistoryRepository;

    public SolutionHistory save(SolutionHistory solutionHistory) { return solutionHistoryRepository.save(solutionHistory); }
}
