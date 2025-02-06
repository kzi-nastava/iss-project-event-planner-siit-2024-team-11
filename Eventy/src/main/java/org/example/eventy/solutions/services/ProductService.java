package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ProductService {
    @Autowired
    private SolutionRepository solutionRepository;

    public Product getProduct(Long productId) {
        return (Product) solutionRepository.findById(productId).orElse(null);
    }

    public Product save(Product product) {
        return solutionRepository.save(product);
    }
}
