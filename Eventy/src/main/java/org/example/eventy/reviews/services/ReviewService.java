package org.example.eventy.reviews.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.example.eventy.reviews.models.Review;
import org.example.eventy.reviews.repositories.ReviewRepository;
import org.example.eventy.solutions.models.Product;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> getPendingReviews(Pageable pageable) {
        //Pageable pageable = PageRequest.of(page, size, Sort.by("criteria").descending());
        return reviewRepository.findAllByStatusOrderByIdDesc(pageable, Status.PENDING);
    }

    public Page<Review> getAcceptedReviews(Pageable pageable) {
        //Pageable pageable = PageRequest.of(page, size, Sort.by("criteria").descending());
        return reviewRepository.findAllByStatusOrderByIdDesc(pageable, Status.ACCEPTED);
    }

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review updateReview(Review review, UpdateReviewDTO updateReviewDTO) {
        review.setComment(updateReviewDTO.getComment());
        review.setGrade(updateReviewDTO.getGrade());
        review.setStatus(updateReviewDTO.getStatus());

        return saveReview(review);
    }

    public Review deleteReview(Review review) {
        review.setStatus(Status.DENIED);
        review.setDeleted(true);

        return saveReview(review);
    }

    public Review saveReview(Review review) {
        try {
            return reviewRepository.save(review);
        }
        catch (Exception e) {
            return null;
        }
    }
}
