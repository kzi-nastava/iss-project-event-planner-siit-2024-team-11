package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.ProductHistory;
import org.example.eventy.solutions.repositories.ProductHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductHistoryService {
    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    public ProductHistory save(ProductHistory productHistory) {
        return productHistoryRepository.save(productHistory);
    }
}
