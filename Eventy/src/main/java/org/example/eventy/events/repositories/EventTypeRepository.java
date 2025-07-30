package org.example.eventy.events.repositories;

import org.example.eventy.events.models.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    Page<EventType> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);
    List<EventType> findByIsActiveTrue();

    @Query("SELECT COUNT(et) FROM EventType et WHERE LOWER(et.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(et.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    long findCountSearch(@Param("search") String search);
}
