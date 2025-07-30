package org.example.eventy.solutions.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Budget;
import org.example.eventy.events.models.BudgetItem;
import org.example.eventy.solutions.dtos.CategoryDTO;
import org.example.eventy.solutions.dtos.categories.CreateCategoryDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionCategoryRepository;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SolutionCategoryService {

    @Autowired
    private SolutionCategoryRepository solutionCategoryRepository;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private SolutionRepository solutionRepository;

    public long getAcceptedCategoryCount() {
        return solutionCategoryRepository.countByStatus(Status.ACCEPTED);
    }

    public long getCategoryRequestCount() {
        return solutionCategoryRepository.countByStatus(Status.PENDING);
    }

    public CategoryWithIDDTO createCategory(CreateCategoryDTO newCategory) {
        Category category = new Category();
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        category.setStatus(newCategory.getStatus());

        return new CategoryWithIDDTO(solutionCategoryRepository.save(category));
    }

    public Page<CategoryWithIDDTO> getCategoryRequests(Pageable pageable) {
        return solutionCategoryRepository.findByStatus(pageable, Status.PENDING).map(CategoryWithIDDTO::new);
    }

    public Page<CategoryWithIDDTO> getAcceptedCategories(Pageable pageable) {
        return solutionCategoryRepository.findByStatus(pageable, Status.ACCEPTED).map(CategoryWithIDDTO::new);
    }

    public List<Category> getAcceptedCategories() {
        return solutionCategoryRepository.findAllByStatus(Status.ACCEPTED);
    }

    public Category getCategory(Long id) {
        Optional<Category> category = solutionCategoryRepository.findById(id);
        return category.orElse(null);

    }

    public Category updateCategory(CategoryWithIDDTO newCategory) {
        // check if exists
        Optional<Category> oldCategory = solutionCategoryRepository.findById(newCategory.getId());
        if (!oldCategory.isPresent()) {
            return null;
        }

        // set new values
        Category category = oldCategory.get();
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        category.setStatus(newCategory.getStatus());

        // save and return updated DTO
        return solutionCategoryRepository.save(category);
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> oldCategory = solutionCategoryRepository.findById(id);
        if (oldCategory.isEmpty()) {
            return false;
        }
        solutionCategoryRepository.deleteById(id);
        return true;
    }

    public Category acceptCategory(Long id) {
        Optional<Category> storedCategory = solutionCategoryRepository.findById(id);
        if (storedCategory.isPresent()) {
            Category acceptedCategory = storedCategory.get();
            if (acceptedCategory.getStatus() != Status.PENDING) {
                return null;
            }
            acceptedCategory.setStatus(Status.ACCEPTED);
            makeAffectedSolutionsVisible(acceptedCategory.getId());
            return solutionCategoryRepository.save(acceptedCategory);
        }
        return null;
    }

    public Category changeCategory(CategoryWithIDDTO newCategory) {
        Optional<Category> oldCategory = solutionCategoryRepository.findById(newCategory.getId());
        if (oldCategory.isPresent()) {
            Category category = oldCategory.get();
            if (category.getStatus() != Status.PENDING) {
                return null;
            }
            category.setName(newCategory.getName());
            category.setDescription(newCategory.getDescription());
            category.setStatus(Status.ACCEPTED);
            makeAffectedSolutionsVisible(category.getId());
            return solutionCategoryRepository.save(category);
        }
        return null;
    }

    public List<Category> getAllRemaining(Budget budget) {
        List<Long> existingCategoryIds = new ArrayList<>();
        for (BudgetItem item: budget.getBudgetedItems()) {
            existingCategoryIds.add(item.getCategory().getId());
        }
        return solutionCategoryRepository.findAllExceptFollowingIds(existingCategoryIds);
    }

    private void makeAffectedSolutionsVisible(Long categoryId) {
        List<Solution> affectedSolutions = solutionService.getAllByCategoryId(categoryId);
        for (Solution solution: affectedSolutions) {
            solution.setVisible(true);
            solutionRepository.save(solution);
        }
    }
}
