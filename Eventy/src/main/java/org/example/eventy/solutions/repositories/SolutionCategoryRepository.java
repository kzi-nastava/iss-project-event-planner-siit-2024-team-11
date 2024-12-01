package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;

public interface SolutionCategoryRepository extends JpaRepository<Category, Long> {

    public Page<Category> findAll(Pageable pageable);
}
