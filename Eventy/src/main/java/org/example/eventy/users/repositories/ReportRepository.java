package org.example.eventy.users.repositories;

import org.example.eventy.common.models.Status;
import org.example.eventy.users.models.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAllByStatusOrderByIdDesc(Pageable pageable, Status status);

    /*
    @Query("SELECT r.grade FROM Review r WHERE r.event.id = :eventId")
    List<Integer> findAllGradesForEvent(@Param("eventId") Long eventId);
  
    Boolean existsByGraderIdAndSolutionId(Long userId, Long solutionId);*/
}
