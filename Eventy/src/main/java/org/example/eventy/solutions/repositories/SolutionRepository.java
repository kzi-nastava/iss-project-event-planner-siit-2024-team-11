package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    @Query("SELECT s FROM Solution s WHERE s.provider.id = :providerId " +
            "AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Solution> findByProvider(@Param("providerId") Long providerId,
                                @Param("search") String search,
                                Pageable pageable);

    @Query("SELECT COUNT(s) FROM Solution s WHERE s.provider.id = :providerId")
    long countByProviderId(@Param("providerId") Long providerId);

    @Query("SELECT s FROM Solution s WHERE s.id IN " +
            "(SELECT us.id FROM User u JOIN u.favoriteSolutions us WHERE u.id = :userId)" +
            "AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Solution> findUsersFavoriteSolutions(@Param("userId") Long userId,
                                        @Param("search") String search,
                                        Pageable pageable);

    @Query("""
           SELECT COUNT(s) FROM Solution s WHERE s.id IN (
                SELECT us.id FROM User u JOIN u.favoriteSolutions us WHERE u.id = :userId)
           """)
    long countUsersFavoriteSolutions(@Param("userId") Long userId);

    @Query("""
           SELECT s
           FROM Solution s
           LEFT JOIN s.eventTypes et
           WHERE (:search = '' OR s.name ILIKE ('%' || :search || '%'))
             AND (:type = 'Any' OR ((:type = 'Product' AND TYPE(s) = Product) OR ( :type = 'Service' AND TYPE(s) = Service)))
             AND (:categories IS NULL OR s.category.name IN :categories)
             AND (:eventTypes IS NULL OR EXISTS (
               SELECT 1
               FROM s.eventTypes et
               WHERE et.name IN :eventTypes
               AND SIZE(s.eventTypes) = :eventTypesSize
             ))
             AND (:company IS NULL OR :company = '' OR LOWER(s.provider.name) = LOWER(:company))
             AND (:minPrice IS NULL OR :maxPrice IS NULL OR ((s.price - (s.price * s.discount / 100)) BETWEEN :minPrice AND :maxPrice))
             AND (s.isAvailable = :isAvailable)
             AND (s.isVisible = TRUE)
             AND (s.isDeleted = FALSE)
             AND (CAST(:startDate AS timestamp) IS NULL OR CAST(:endDate AS timestamp) IS NULL
                OR NOT EXISTS (
                   SELECT r
                   FROM Reservation r
                   WHERE r.selectedService.id = s.id
                      AND (CAST(:startDate AS timestamp) <= r.reservationEndDateTime AND CAST(:startDate AS timestamp) >= r.reservationStartDateTime)
                      OR (CAST(:endDate AS timestamp) >= r.reservationStartDateTime AND CAST(:endDate AS timestamp) <= r.reservationEndDateTime)))
           """)
    Page<Solution> findAll(@Param("search") String search,
                           @Param("type") String type,
                           @Param("categories") List<String> categories,
                           @Param("eventTypes") List<String> eventTypes,
                           @Param("eventTypesSize") Integer eventTypesSize,
                           @Param("company") String company,
                           @Param("minPrice") Double minPrice,
                           @Param("maxPrice") Double maxPrice,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           @Param("isAvailable") Boolean isAvailable,
                           Pageable pageable);

    @Query("SELECT s FROM Solution s ORDER BY s.id DESC")
    ArrayList<Solution> findFeaturedSolutions(Pageable pageable);
}
