package org.example.eventy.reviews.controllers;

import org.example.eventy.reviews.dtos.CreateReviewDTO;
import org.example.eventy.reviews.dtos.CreatedReviewDTO;
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

    // GET "/api/reviews/5"
    @GetMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {
        ReviewDTO review = reviewService.getReview(reviewId);

        if (reviewId == 5) {
            return new ResponseEntity<ReviewDTO>(review, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    // GET "/api/reviews/pending"
    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReviewDTO>> getPendingReviews(Pageable pageable) {
        ArrayList<ReviewDTO> pendingReviews = reviewService.getPendingReviews(pageable);
        return new ResponseEntity<Collection<ReviewDTO>>(pendingReviews, HttpStatus.OK);
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
        "status": "PENDING"
    }
    */
    // PUT "/api/reviews/5"
    @PutMapping(value = "/{reviewId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody UpdateReviewDTO updatedReview, @PathVariable Long reviewId) {
        if(reviewId == 5) {
            ReviewDTO review = reviewService.getReview(reviewId);
            review = reviewService.updateReview(review, updatedReview);

            return new ResponseEntity<ReviewDTO>(review, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    // DELETE "/api/reviews/5"
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId) {
        if(reviewId == 5) {
            ReviewDTO review = reviewService.getReview(reviewId);
            review = reviewService.deleteReview(review);

            return new ResponseEntity<ReviewDTO>(review, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NO_CONTENT);
    }

    // POST "/api/reviews"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedReviewDTO> createReview(@RequestBody CreateReviewDTO reviewDTO) {
        // create Review in service
        CreatedReviewDTO responseDTO = new CreatedReviewDTO();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
