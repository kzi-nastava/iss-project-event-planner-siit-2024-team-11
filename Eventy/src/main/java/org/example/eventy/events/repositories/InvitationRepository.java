package org.example.eventy.events.repositories;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByGuestEmailAndStatusEquals(String guestEmail, Status status);
}
