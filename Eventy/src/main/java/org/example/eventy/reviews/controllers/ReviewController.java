package org.example.eventy.reviews.controllers;

import org.example.eventy.reviews.dtos.ReviewDTO;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.example.eventy.reviews.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // GET "/api/reviews/pending/5"
    @GetMapping(value = "/pending/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> getPendingReview(@PathVariable Long reviewId) {
        ReviewDTO pendingReview = reviewService.getPendingReview(reviewId);

        if (reviewId == 5) {
            return new ResponseEntity<ReviewDTO>(pendingReview, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reviews/pending"
    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReviewDTO>> getPendingReviews(Pageable pageable) {
        ArrayList<ReviewDTO> pendingReviews = reviewService.getPendingReviews(pageable);
        return new ResponseEntity<Collection<ReviewDTO>>(pendingReviews, HttpStatus.OK);
    }

    // GET "/api/reviews/accepted/5"
    @GetMapping(value = "/accepted/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> getAcceptedReview(@PathVariable Long reviewId) {
        ReviewDTO acceptedReview = reviewService.getAcceptedReview(reviewId);

        if (reviewId == 5) {
            return new ResponseEntity<ReviewDTO>(acceptedReview, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reviews/accepted"
    @GetMapping(value = "/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReviewDTO>> getAcceptedReviews(Pageable pageable) {
        ArrayList<ReviewDTO> acceptedReviews = reviewService.getAcceptedReviews(pageable);
        return new ResponseEntity<Collection<ReviewDTO>>(acceptedReviews, HttpStatus.OK);
    }

    /*
    {
        "comment": "Updated review comment",
        "grade": 4,
        "status": PENDING
    }
    */
    // PUT "/api/reviews/5"
    @PutMapping(value = "/{reviewId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody UpdateReviewDTO updatedReview, @PathVariable Long reviewId) {
        if(reviewId == 5) {
            ReviewDTO pendingReview = reviewService.getPendingReview(reviewId);
            pendingReview = reviewService.updateReview(pendingReview, updatedReview);

            return new ResponseEntity<ReviewDTO>(pendingReview, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    // DELETE "/api/reviews/5"
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId) {
        if(reviewId == 5) {
            ReviewDTO pendingReview = reviewService.getPendingReview(reviewId);
            pendingReview = reviewService.deleteReview(pendingReview);

            return new ResponseEntity<ReviewDTO>(pendingReview, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NO_CONTENT);
    }
}
