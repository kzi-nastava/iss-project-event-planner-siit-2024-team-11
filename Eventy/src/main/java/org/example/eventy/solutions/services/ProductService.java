package org.example.eventy.solutions.services;

import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class ProductService {
    @Autowired
    private SolutionRepository solutionRepository;

    public Solution getProduct(Long productId) {
        return solutionRepository.findById(productId).orElse(null);
    }

    public Product save(Product product) {
        return solutionRepository.save(product);
    }

    public Product delete(Long productId) {
        try {
            Product product = (Product) getProduct(productId);
            product.setDeleted(true);
            return solutionRepository.save(product);
        }
        catch (Exception e) {
            return null;
        }
    }
}
