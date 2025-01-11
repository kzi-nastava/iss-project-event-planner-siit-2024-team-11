package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class SolutionService {
    @Autowired
    private SolutionRepository solutionRepository;

    public Page<Solution> getSolutions(String search, String type, ArrayList<String> categories, ArrayList<String> eventTypes, String company, double minPrice, double maxPrice, LocalDateTime startDate, LocalDateTime endDate, Boolean isAvailable, Pageable pageable) {
        return solutionRepository.findAll(search, type, categories, eventTypes, eventTypes == null ? 0 : eventTypes.size(), company, minPrice, maxPrice, startDate, endDate, isAvailable, pageable);
    }

    public Solution getSolution(Long solutionId) {
        return solutionRepository.findById(solutionId).orElse(null);
    }

    public ArrayList<Solution> getFeaturedSolutions() {
        Pageable pageable = PageRequest.of(0, 5);
        return solutionRepository.findFeaturedSolutions(pageable);
    }

    public List<Solution> getSolutionsByProvider(Long providerId, String search, Pageable pageable) {
        return solutionRepository.findByProvider(providerId, search, pageable).getContent();
    }

    public long getSolutionsByProviderCount(Long providerId) {
        return solutionRepository.countByProviderId(providerId);
    }

    public List<Solution> getFavoriteSolutionsByUser(Long userId, String search, Pageable pageable) {
        return solutionRepository.findUsersFavoriteSolutions(userId, search, pageable).getContent();
    }

    public long getFavoriteSolutionsByUserCount(Long userId) {
        return solutionRepository.countUsersFavoriteSolutions(userId);
    }
}
