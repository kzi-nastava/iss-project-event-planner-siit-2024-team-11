package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class SolutionService {
    @Autowired
    private SolutionRepository solutionRepository;

    public Page<Solution> getSolutions(String search, String type, String categories, String eventTypes, String company, double minPrice, double maxPrice, LocalDateTime startDate, LocalDateTime endDate, Boolean isAvailable, Pageable pageable) {
        int page = pageable.getPageNumber();
        if (page < 0) page = 0;

        int pageSize = pageable.getPageSize();
        if (pageSize != 1 && pageSize != 2 && pageSize != 5 && pageSize != 10) pageSize = 5;

        String sortField = pageable.getSort().isEmpty() ? "id" : pageable.getSort().iterator().next().getProperty();
        String sortDirection = pageable.getSort().isEmpty() ? "asc" : pageable.getSort().iterator().next().getDirection().name();
        String sort = (sortField + "," + sortDirection).toLowerCase();

        List<Solution> solutions = solutionRepository.findAll(search, type, categories, eventTypes, company, minPrice, maxPrice, startDate, endDate, isAvailable, page, pageSize, sort);
        int total = solutionRepository.findTotalCount(search, type, categories, eventTypes, company, minPrice, maxPrice, startDate, endDate, isAvailable);

        return new PageImpl<>(solutions, PageRequest.of(page, pageSize), total);

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

    public ArrayList<String> getAllUniqueEventTypesForSolutions() {
        return solutionRepository.findAllUniqueEventTypeNamesForSolutions();
    }

    public ArrayList<String> getAllUniqueCategoriesForSolutions() {
        return solutionRepository.findAllUniqueCategoryNamesForSolutions();
    }

    public ArrayList<String> getAllUniqueCompaniesForSolutions() {
        return solutionRepository.findAllUniqueCompanyNamesForSolutions();
    }

    public boolean replaceCategoryForSolutionsWithOldCategory(Category oldCategory, Category newCategory) {
        return solutionRepository.updateCategoryForAllSolutions(oldCategory, newCategory) == 1;
    }

    public Solution findSolutionWithPendingCategory(Category category) {
        return solutionRepository.findByCategory(category).get(0);
    }

    public List<Solution> findAllByCategory(Category category) {
        return solutionRepository.findByCategory(category);
    }
}
