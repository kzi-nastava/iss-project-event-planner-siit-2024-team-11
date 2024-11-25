package org.example.eventy.reviews.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.reviews.dtos.ReviewDTO;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReviewService {

    /*@Autowired
    private ReviewRepository reviewRepository;*/

    public ReviewDTO getReview(Long reviewId) {
        ReviewDTO review = new ReviewDTO(
            reviewId,
            "Great event, well-organized and fun!",
            5,
            "johndoe123@gmail.com",
            "exit.festival@gmail.com",
            "EXIT 2024",
            Status.PENDING,
            false
        );

        return review;
    }

    public ArrayList<ReviewDTO> getPendingReviews(Pageable pageable) {
        ReviewDTO pendingReview1 = new ReviewDTO(
            1L,
            "Great event, well-organized and fun!",
            5,
            "johndoe123@gmail.com",
            "exit.festival@gmail.com",
            "EXIT 2024",
            Status.PENDING,
            false
        );

        ReviewDTO pendingReview2 = new ReviewDTO(
            2L,
            "The cake was decent, but there is room for improvement.",
            3,
            "jane002@gmail.com",
            "ns.best.cakes@gmail.com",
            "Wedding cake - Sweet 16",
            Status.PENDING,
            false
        );

        ArrayList<ReviewDTO> pendingReviews = new ArrayList<>();
        pendingReviews.add(pendingReview1);
        pendingReviews.add(pendingReview2);

        return pendingReviews;
    }

    public ArrayList<ReviewDTO> getAcceptedReviews(Pageable pageable) {
        ReviewDTO acceptedReview1 = new ReviewDTO(
            1L,
            "Great event, well-organized and fun!",
            5,
            "johndoe123@gmail.com",
            "exit.festival@gmail.com",
            "EXIT 2024",
            Status.ACCEPTED,
            false
        );

        ReviewDTO acceptedReview2 = new ReviewDTO(
            2L,
            "The cake was decent, but there is room for improvement.",
            3,
            "jane002@gmail.com",
            "ns.best.cakes@gmail.com",
            "Wedding cake - Sweet 16",
            Status.ACCEPTED,
            false
        );

        ArrayList<ReviewDTO> acceptedReviews = new ArrayList<>();
        acceptedReviews.add(acceptedReview1);
        acceptedReviews.add(acceptedReview2);

        return acceptedReviews;
    }

    public ReviewDTO updateReview(ReviewDTO review, UpdateReviewDTO updatedReview) {
        review.setComment(updatedReview.getComment());
        review.setGrade(updatedReview.getGrade());
        review.setStatus(updatedReview.getStatus());

        return saveReview(review);
    }

    public ReviewDTO deleteReview(ReviewDTO review) {
        review.setStatus(Status.DENIED);
        review.setDeleted(true);

        return saveReview(review);
    }

    public ReviewDTO saveReview(ReviewDTO pendingReview) {
        return pendingReview;
    }
}
