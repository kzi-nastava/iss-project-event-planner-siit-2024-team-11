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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> getPendingReviews(Pageable pageable) {
        return reviewRepository.findAllByStatusOrderByIdDesc(pageable, Status.PENDING);
    }

    public Page<Review> getAcceptedReviews(Pageable pageable) {
        return reviewRepository.findAllByStatusOrderByIdDesc(pageable, Status.ACCEPTED);
    }

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review acceptReview(Review review) {
        review.setStatus(Status.ACCEPTED);
        return saveReview(review);
    }

    public Review declineReview(Review review) {
        review.setStatus(Status.DENIED);
        review.setDeleted(true);
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

    public List<Integer> getGradesForEvent(Long eventId) {
        return reviewRepository.findAllGradesForEvent(eventId);
    }
  
    public Boolean isSolutionReviewedByUser(Long userId, Long solutionId) {
        return reviewRepository.existsByGraderIdAndSolutionId(userId, solutionId);
    }

    public List<Review> getReviewsForSolution(Long solutionId) {
        return reviewRepository.findAllBySolutionId(solutionId);
    }
}
