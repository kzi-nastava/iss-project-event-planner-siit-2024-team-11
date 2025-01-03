package org.example.eventy.events.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Invitation;
import org.example.eventy.events.repositories.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {
    @Autowired
    private InvitationRepository invitationRepository;

    public Invitation getInvitation(Long invitationId) {
        return invitationRepository.findById(invitationId).orElse(null);
    }

    public List<Invitation> getInvitations() {
        return invitationRepository.findAll();
    }

    public List<Invitation> getPendingInvitationsByGuestEmail(String guestEmail) {
        return invitationRepository.findByGuestEmailAndStatusEquals(guestEmail, Status.PENDING);
    }

    public Invitation save(Invitation invitation) {
        try {
            return invitationRepository.save(invitation);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
