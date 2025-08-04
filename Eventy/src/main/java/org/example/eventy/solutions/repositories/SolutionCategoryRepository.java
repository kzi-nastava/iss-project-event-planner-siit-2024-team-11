package org.example.eventy.solutions.repositories;

import org.example.eventy.common.models.Status;
import org.example.eventy.solutions.models.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolutionCategoryRepository extends JpaRepository<Category, Long> {

    public Page<Category> findAll(Pageable pageable);

    public Page<Category> findByStatus(Pageable pageable, Status status);

    public List<Category> findAllByStatus(Status status);

    public long countByStatus(Status status);

    @Query("SELECT c FROM Category c WHERE c.id NOT IN (:ids) AND c.status = :status")
    public List<Category> findAllExceptFollowingIds(List<Long> ids, Status status);
}
