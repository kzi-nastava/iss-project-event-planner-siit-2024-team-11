package org.example.eventy.reviews.repositories;

import org.example.eventy.reviews.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r " +
            "WHERE (:status = '' OR CAST(r.status AS string) = :status)")
    Page<Review> findAll(Pageable pageable, String status);
}
