package org.example.eventy.solutions.services;

import org.example.eventy.solutions.dtos.CategoryDTO;
import org.example.eventy.solutions.dtos.categories.CreateCategoryDTO;
import org.example.eventy.solutions.dtos.categories.CategoryWithIDDTO;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.repositories.SolutionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SolutionCategoryService {

    @Autowired
    private SolutionCategoryRepository solutionCategoryRepository;

    public CategoryWithIDDTO createCategory(CreateCategoryDTO newCategory) {
        Category category = new Category();
        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
        category.setStatus(newCategory.getStatus());

        return new CategoryWithIDDTO(solutionCategoryRepository.save(category));
    }

    public Page<CategoryWithIDDTO> getCategories(Pageable pageable) {
        return solutionCategoryRepository.findAll(pageable).map(CategoryWithIDDTO::new);
    }

    public CategoryWithIDDTO updateCategory(CategoryWithIDDTO newCategory) {
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
        return new CategoryWithIDDTO(solutionCategoryRepository.save(category));
    }

    public boolean deleteCategory(Long id) {
        Optional<Category> oldCategory = solutionCategoryRepository.findById(id);
        if (oldCategory.isEmpty()) {
            return false;
        }
        solutionCategoryRepository.deleteById(id);
        return true;
    }
}
