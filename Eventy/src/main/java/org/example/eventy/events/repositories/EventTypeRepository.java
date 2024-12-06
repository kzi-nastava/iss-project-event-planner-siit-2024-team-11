package org.example.eventy.events.repositories;

import org.example.eventy.events.models.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    Page<EventType> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);
    @Query("SELECT e FROM EventType e " +
            "WHERE e.isActive = true " +
            "AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<EventType> findActiveTypes(@Param("search") String search, Pageable pageable);
}
