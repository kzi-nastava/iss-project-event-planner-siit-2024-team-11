package org.example.eventy.reviews.services;

import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.example.eventy.reviews.models.Review;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.users.models.EventOrganizer;
import org.example.eventy.users.models.SolutionProvider;
import org.example.eventy.users.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReviewService {

    /*@Autowired
    private ReviewRepository reviewRepository;*/

    public ArrayList<Review> getPendingReviews() {
        ArrayList<Review> pendingReviews = generateReviewExamples(1);
        return pendingReviews;
    }

    public ArrayList<Review> getAcceptedReviews() {
        ArrayList<Review> acceptedReviews = generateReviewExamples(2);
        return acceptedReviews;
    }

    public Review getReview(Long reviewId) {
        ArrayList<Review> reviews = generateReviewExamples(3);
        Review review = reviews.get(0);
        review.setId(reviewId);

        return review;
    }

    public Review updateReview(Long reviewId, UpdateReviewDTO updatedReview) {
        Review review = getReview(reviewId);

        review.setComment(updatedReview.getComment());
        review.setGrade(updatedReview.getGrade());
        review.setStatus(updatedReview.getStatus());

        return saveReview(review);
    }

    public Review deleteReview(Long reviewId) {
        Review review = getReview(reviewId);

        review.setStatus(Status.DENIED);
        review.setDeleted(true);

        return saveReview(review);
    }

    public Review saveReview(Review review) {
        return review;
    }

    public ArrayList<Review> generateReviewExamples(int type) {
        Event event = new Event();
        Solution solution = new Solution();
        EventOrganizer organizer = new EventOrganizer();
        organizer.setEmail("johndoe123@gmail.com");
        event.setOrganiser(organizer);
        SolutionProvider provider = new SolutionProvider();
        provider.setEmail("exit.festival@gmail.com");
        solution.setProvider(provider);
        User user = new User();
        user.setEmail("sender@gmail.com");

        Review review1 = new Review(
            1L,
            user,
            null,
            solution,
            type == 1? "PENDING - Great event, well-organized and fun!" : type == 2 ? "ACCEPTED - Great event, well-organized and fun!" : "Great event, well-organized and fun!",
            5,
            type == 1 ? Status.PENDING : Status.ACCEPTED,
            false
        );

        Review review2 = new Review(
            2L,
            user,
            event,
            null,
            type == 1? "PENDING - The cake was decent, but there is room for improvement." : type == 2 ? "ACCEPTED - The cake was decent, but there is room for improvement." : "The cake was decent, but there is room for improvement.",
            3,
            type == 1 ? Status.PENDING : Status.ACCEPTED,
            false
        );

        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);

        return reviews;
    }
}
