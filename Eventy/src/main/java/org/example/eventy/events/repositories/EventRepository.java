package org.example.eventy.events.repositories;

import org.example.eventy.events.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.organiser.id = :eventOrganizerId " +
            "AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Event> findByOrganizer(@Param("eventOrganizerId") Long eventOrganizerId,
                                              @Param("search") String search,
                                              Pageable pageable);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.organiser.id = :eventOrganizerId")
    long countByEventOrganizerId(@Param("eventOrganizerId") Long eventOrganizerId);

    @Query("SELECT e FROM Event e WHERE e.id IN " +
            "(SELECT ue.id FROM User u JOIN u.favoriteEvents ue WHERE u.id = :userId)" +
            "AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Event> findUsersFavoriteEvents(@Param("userId") Long userId,
                                               @Param("search") String search,
                                               Pageable pageable);

    @Query("SELECT COUNT(e) FROM Event e WHERE e.id IN " +
            "(SELECT ue.id FROM User u JOIN u.favoriteEvents ue WHERE u.id = :userId)")
    long countUsersFavoriteEvents(@Param("userId") Long userId);
}
