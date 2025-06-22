package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.ServiceHistory;
import org.example.eventy.solutions.repositories.ServiceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceHistoryService {
    @Autowired
    private ServiceHistoryRepository serviceHistoryRepository;

    public ServiceHistory save(ServiceHistory serviceHistory) {
        return serviceHistoryRepository.save(serviceHistory);
    }
}
