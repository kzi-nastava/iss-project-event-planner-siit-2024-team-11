package org.example.eventy.events.repositories;

import org.example.eventy.events.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Query("SELECT e FROM Event e " +
            "WHERE e.organiser.id = :userId " +
            "AND e.date BETWEEN :startDate AND :endDate")
    List<Event> findOrganizedEventsByUserBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT e FROM User u JOIN u.acceptedEvents e " +
            "WHERE u.id = :userId AND e.date BETWEEN :startDate AND :endDate")
    List<Event> findAttendingEventsByUserBetween(@Param("userId") Long userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate")LocalDate endDate);

    @Query("SELECT e FROM Event e " +
            "WHERE (:search = '' OR e.name ILIKE ('%' || :search || '%')) " +
            "   OR (:search = '' OR e.description ILIKE ('%' || :search || '%')) " +
            "AND (:maxParticipants IS NULL OR e.maxNumberParticipants <= :maxParticipants) " +
            "AND (:location IS NULL OR e.location.name = :location) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR CAST(:endDate AS timestamp) IS NULL OR e.date BETWEEN CAST(:startDate AS timestamp) AND CAST(:endDate AS timestamp)) " +
            "AND (:eventTypes IS NULL OR e.type.name IN :eventTypes)")
    Page<Event> findAll(@Param("search") String search,
                        @Param("eventTypes") List<String> eventTypes,
                        @Param("maxParticipants") Integer maxParticipants,
                        @Param("location") String location,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);

    @Query("SELECT e FROM Event e ORDER BY e.id DESC")
    ArrayList<Event> findFeaturedEvents(Pageable pageable);
}
