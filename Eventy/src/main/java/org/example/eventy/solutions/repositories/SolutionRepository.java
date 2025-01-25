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

    @Query(value = """
            SELECT cancellation_deadline, discount, is_available, is_deleted, is_visible,
                   max_reservation_time, min_reservation_time, price, reservation_deadline, reservation_type,
                   category_id, id, product_history_id, provider_id, service_history_id, type, description, name, specifics
            FROM (
                SELECT cancellation_deadline, discount, is_available, is_deleted, is_visible,
                       max_reservation_time, min_reservation_time, price, reservation_deadline, reservation_type,
                       category_name, category_id, a.id AS id, product_history_id, provider_id, u.name AS provider_name,
                       service_history_id, type, a.description AS description, a.name AS name, specifics, et
                FROM (
                    SELECT cancellation_deadline, discount, is_available, f.is_deleted AS is_deleted, is_visible,
                           max_reservation_time, min_reservation_time, price, reservation_deadline, reservation_type,
                           cat.name AS category_name, category_id, f.id AS id, product_history_id, provider_id,
                           service_history_id, type, f.description AS description, f.name AS name, specifics, et
                    FROM (
                        SELECT
                            s.*,
                             STRING_AGG(CAST(
                                (SELECT name FROM event_types WHERE st.event_type_id = id)
                                AS TEXT), ',') AS et
                        FROM solutions s
                        LEFT JOIN suggested_event_types st ON s.id = st.solution_id	
                        GROUP BY s.id
                    ) AS f
                    LEFT JOIN categories cat ON cat.id = category_id
                ) AS a
                LEFT JOIN users u ON u.id = provider_id
            ) AS b
            WHERE (
                    ((et IS NULL OR et = '') AND :eventTypes IS NULL OR :eventTypes = '')
                  OR 
                    (et ILIKE ('%' || :eventTypes || '%'))
                  )
            AND (
                (:search = '' OR b.name ILIKE ('%' || :search || '%'))
                OR (:search IS NULL OR :search = '' OR b.description ILIKE ('%' || :search || '%'))
            )
            AND (:type = 'Any' OR ((:type = 'Product' AND b.type LIKE 'Product') OR ( :type = 'Service' AND b.type LIKE 'Service')))
            AND (:categories IS NULL OR :categories = '' OR :categories ILIKE ('%' || b.category_name || '%'))
            AND (:company IS NULL OR :company = '' OR LOWER(b.provider_name) = LOWER(:company))
            AND (:minPrice IS NULL OR :maxPrice IS NULL OR ((b.price - (b.price * b.discount / 100)) BETWEEN :minPrice AND :maxPrice))
            AND (b.is_available = :isAvailable)
            AND (b.is_visible = TRUE)
            AND (b.is_deleted = FALSE)
            AND (CAST(:startDate AS timestamp) IS NULL OR CAST(:endDate AS timestamp) IS NULL
                 OR NOT EXISTS (
                    SELECT 1
                    FROM reservations r
                    WHERE r.selected_service_id = b.id
                      AND (
                          (CAST(:startDate AS timestamp) <= r.reservation_end_date_time AND CAST(:startDate AS timestamp) >= r.reservation_start_date_time)
                          OR (CAST(:endDate AS timestamp) >= r.reservation_start_date_time AND CAST(:endDate AS timestamp) <= r.reservation_end_date_time)
                      )
                 ))
            ORDER BY
              CASE WHEN :sort = 'id,asc' THEN b.id END ASC,
              CASE WHEN :sort = 'category,asc' THEN b.category_name END ASC,
              CASE WHEN :sort = 'name,asc' THEN b.name END ASC,
              CASE WHEN :sort = 'price,asc' THEN (b.price - (b.price * b.discount / 100)) END ASC,
              CASE WHEN :sort = 'price,desc' THEN (b.price - (b.price * b.discount / 100)) END DESC
            LIMIT :pageSize OFFSET (:page * :pageSize);
            """, nativeQuery = true)
    List<Solution> findAll(@Param("search") String search,
                           @Param("type") String type,
                           @Param("categories") String categories,
                           @Param("eventTypes") String eventTypes,
                           @Param("company") String company,
                           @Param("minPrice") Double minPrice,
                           @Param("maxPrice") Double maxPrice,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           @Param("isAvailable") Boolean isAvailable,
                           @Param("page") int page,
                           @Param("pageSize") int pageSize,
                           @Param("sort") String sort);

    @Query(value = """
            SELECT COUNT(*)
            FROM (
                SELECT cancellation_deadline, discount, is_available, is_deleted, is_visible,
                   max_reservation_time, min_reservation_time, price, reservation_deadline, reservation_type,
                   category_id, id, product_history_id, provider_id, service_history_id, type, description, name, specifics
                FROM (
                    SELECT cancellation_deadline, discount, is_available, is_deleted, is_visible,
                           max_reservation_time, min_reservation_time, price, reservation_deadline, reservation_type,
                           category_name, category_id, a.id AS id, product_history_id, provider_id, u.name AS provider_name,
                           service_history_id, type, a.description AS description, a.name AS name, specifics, et
                    FROM (
                        SELECT cancellation_deadline, discount, is_available, f.is_deleted AS is_deleted, is_visible,
                               max_reservation_time, min_reservation_time, price, reservation_deadline, reservation_type,
                               cat.name AS category_name, category_id, f.id AS id, product_history_id, provider_id,
                               service_history_id, type, f.description AS description, f.name AS name, specifics, et
                        FROM (
                            SELECT
                                s.*,
                                 STRING_AGG(CAST(
                                    (SELECT name FROM event_types WHERE st.event_type_id = id)
                                    AS TEXT), ',') AS et
                            FROM solutions s
                            LEFT JOIN suggested_event_types st ON s.id = st.solution_id	
                            GROUP BY s.id
                        ) AS f
                        LEFT JOIN categories cat ON cat.id = category_id
                    ) AS a
                    LEFT JOIN users u ON u.id = provider_id
                ) AS b
                WHERE (
                        ((et IS NULL OR et = '') AND :eventTypes IS NULL OR :eventTypes = '')
                      OR 
                        (et ILIKE ('%' || :eventTypes || '%'))
                      )
                AND (
                    (:search = '' OR b.name ILIKE ('%' || :search || '%'))
                    OR (:search IS NULL OR :search = '' OR b.description ILIKE ('%' || :search || '%'))
                )
                AND (:type = 'Any' OR ((:type = 'Product' AND b.type LIKE 'Product') OR ( :type = 'Service' AND b.type LIKE 'Service')))
                AND (:categories IS NULL OR :categories = '' OR :categories ILIKE ('%' || b.category_name || '%'))
                AND (:company IS NULL OR :company = '' OR LOWER(b.provider_name) = LOWER(:company))
                AND (:minPrice IS NULL OR :maxPrice IS NULL OR ((b.price - (b.price * b.discount / 100)) BETWEEN :minPrice AND :maxPrice))
                AND (b.is_available = :isAvailable)
                AND (b.is_visible = TRUE)
                AND (b.is_deleted = FALSE)
                AND (CAST(:startDate AS timestamp) IS NULL OR CAST(:endDate AS timestamp) IS NULL
                     OR NOT EXISTS (
                        SELECT 1
                        FROM reservations r
                        WHERE r.selected_service_id = b.id
                          AND (
                              (CAST(:startDate AS timestamp) <= r.reservation_end_date_time AND CAST(:startDate AS timestamp) >= r.reservation_start_date_time)
                              OR (CAST(:endDate AS timestamp) >= r.reservation_start_date_time AND CAST(:endDate AS timestamp) <= r.reservation_end_date_time)
                          )
                     ))
            );
            """, nativeQuery = true)
    int findTotalCount(@Param("search") String search,
                       @Param("type") String type,
                       @Param("categories") String categories,
                       @Param("eventTypes") String eventTypes,
                       @Param("company") String company,
                       @Param("minPrice") Double minPrice,
                       @Param("maxPrice") Double maxPrice,
                       @Param("startDate") LocalDateTime startDate,
                       @Param("endDate") LocalDateTime endDate,
                       @Param("isAvailable") Boolean isAvailable);

    @Query("SELECT s FROM Solution s ORDER BY s.id DESC")
    ArrayList<Solution> findFeaturedSolutions(Pageable pageable);

    @Query(value = """
            SELECT name FROM (
                SELECT DISTINCT et.name AS name, et.id AS id
                FROM suggested_event_types st
                LEFT JOIN event_types et ON st.event_type_id = et.id
                ORDER BY et.id
            );
           """, nativeQuery = true)
    ArrayList<String> findAllUniqueEventTypeNamesForSolutions();

    @Query("SELECT DISTINCT c.name FROM Category c JOIN Solution s ON c.id = s.category.id ORDER BY c.name ASC")
    ArrayList<String> findAllUniqueCategoryNamesForSolutions();

    @Query("SELECT DISTINCT sp.name FROM SolutionProvider sp JOIN Solution s ON sp.id = s.provider.id ORDER BY sp.name ASC")
    ArrayList<String> findAllUniqueCompanyNamesForSolutions();
}