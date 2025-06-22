package org.example.eventy.solutions.repositories;

import org.example.eventy.solutions.models.ServiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceHistoryRepository extends JpaRepository<ServiceHistory, Long> {
}
