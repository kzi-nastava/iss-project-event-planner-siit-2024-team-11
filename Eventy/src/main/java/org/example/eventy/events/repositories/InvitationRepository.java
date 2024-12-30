package org.example.eventy.events.repositories;

import org.example.eventy.events.models.EventType;
import org.example.eventy.events.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByGuestEmail(String guestEmail);
}
