package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT COUNT(s) FROM Solution s WHERE s.id IN " +
            "(SELECT us.id FROM User u JOIN u.favoriteSolutions us WHERE u.id = :userId)")
    long countUsersFavoriteSolutions(@Param("userId") Long userId);
}
