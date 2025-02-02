package org.example.eventy.reviews.repositories;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.reviews.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByStatusOrderByIdDesc(Pageable pageable, Status status);
    @Query("SELECT r.grade FROM Review r WHERE r.event.id = :eventId")
    List<Integer> findAllGradesForEvent(@Param("eventId") Long eventId);
}
