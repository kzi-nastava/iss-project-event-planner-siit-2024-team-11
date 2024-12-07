package org.example.eventy.users.repositories;

import org.example.eventy.users.models.EventOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizerRepository extends JpaRepository<EventOrganizer, Long> {
    
}
