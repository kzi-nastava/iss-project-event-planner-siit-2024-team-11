package org.example.eventy.solutions.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.events.models.EventType;
import org.example.eventy.solutions.models.Category;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.repositories.SolutionRepository;
import org.example.eventy.users.models.SolutionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

@org.springframework.stereotype.Service
public class ProductService {
    @Autowired
    private SolutionRepository solutionRepository;

    public Page<Solution> getProducts(Pageable pageable) {
        return solutionRepository.findAll(null, null, null, null, null, null, null, null, null, null, null, pageable);
    }

    public Solution getProduct(Long productId) {
        return solutionRepository.findById(productId).orElse(null);
    }
}
