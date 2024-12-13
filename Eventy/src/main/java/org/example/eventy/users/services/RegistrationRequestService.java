package org.example.eventy.users.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.users.dtos.RequestUpdateStatus;
import org.example.eventy.users.models.RegistrationRequest;
import org.example.eventy.users.models.User;
import org.example.eventy.users.repositories.RegistrationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationRequestService {
    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    public RegistrationRequest create(User user) {
        try {
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setUser(user);
            registrationRequest.setDate(LocalDateTime.now());
            registrationRequest.setStatus(Status.PENDING);

            return registrationRequestRepository.save(registrationRequest);
        }
        catch (Exception e) {
            return null;
        }
    }

    public RegistrationRequest get(long id) {
        return registrationRequestRepository.findById(id).orElse(null);
    }

    public RequestUpdateStatus update(long id) {
        try {
            RegistrationRequest registrationRequest = get(id);

            if(registrationRequest.getDate().isBefore(LocalDateTime.now().minusHours(24))) {
                registrationRequest.setStatus(Status.DENIED);
                return RequestUpdateStatus.TOO_LATE;
            }

            registrationRequest.setStatus(Status.ACCEPTED);

            registrationRequestRepository.save(registrationRequest);
            return RequestUpdateStatus.VALID;
        }
        catch (Exception e) {
            return RequestUpdateStatus.NOT_FOUND;
        }
    }

    public User getUserForRequest(Long requestId) {
        RegistrationRequest registrationRequest = registrationRequestRepository.findById(requestId).orElse(null);
        if(registrationRequest == null) return null;

        return registrationRequest.getUser();
    }
}
