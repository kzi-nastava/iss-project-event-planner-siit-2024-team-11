package org.example.eventy.reviews.controllers;

import org.example.eventy.common.models.Status;
import org.example.eventy.reviews.dtos.CreateReviewDTO;
import org.example.eventy.reviews.dtos.CreatedReviewDTO;
import org.example.eventy.reviews.dtos.ReviewDTO;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.example.eventy.reviews.models.Review;
import org.example.eventy.reviews.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // GET "/api/reviews/pending"
    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReviewDTO>> getPendingReviews() {
        ArrayList<Review> pendingReviewModels = reviewService.getPendingReviews();

        ArrayList<ReviewDTO> pendingReviews = new ArrayList<>();
        for (Review review : pendingReviewModels) {
            pendingReviews.add(new ReviewDTO(review));
        }

        return new ResponseEntity<Collection<ReviewDTO>>(pendingReviews, HttpStatus.OK);
    }

    // GET "/api/reviews/accepted"
    @GetMapping(value = "/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReviewDTO>> getAcceptedReviews() {
        ArrayList<Review> acceptedReviewModels = reviewService.getAcceptedReviews();

        ArrayList<ReviewDTO> acceptedReviews = new ArrayList<>();
        for (Review review : acceptedReviewModels) {
            acceptedReviews.add(new ReviewDTO(review));
        }

        return new ResponseEntity<Collection<ReviewDTO>>(acceptedReviews, HttpStatus.OK);
    }

    // GET "/api/reviews/5"
    @GetMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {
        if (reviewId == 5) {
            Review reviewModel = reviewService.getReview(reviewId);
            ReviewDTO review = new ReviewDTO(reviewModel);

            return new ResponseEntity<ReviewDTO>(review, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    /*
//    {
//        "comment": "Updated review comment",
//        "grade": 4,
//        "status": "PENDING"
//    }
    */
    // PUT "/api/reviews/5"
    @PutMapping(value = "/{reviewId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody UpdateReviewDTO updatedReview, @PathVariable Long reviewId) {
        if(reviewId == 5) {
            Review reviewModel = reviewService.updateReview(reviewId, updatedReview);
            ReviewDTO review = new ReviewDTO(reviewModel);

            return new ResponseEntity<ReviewDTO>(review, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    // DELETE "/api/reviews/5"
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId) {
        if(reviewId == 5) {
            Review reviewModel = reviewService.deleteReview(reviewId);
            ReviewDTO review = new ReviewDTO(reviewModel);

            return new ResponseEntity<ReviewDTO>(review, HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NO_CONTENT);
    }

    // POST "/api/reviews"
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedReviewDTO> createReview(@RequestBody CreateReviewDTO reviewDTO) {
        // create Review in service
        CreatedReviewDTO responseDTO = new CreatedReviewDTO();
        responseDTO.setId(1337L);
        responseDTO.setReviewerId(42L);
        responseDTO.setComment("Literally me");
        responseDTO.setGrade(5);
        responseDTO.setStatus(Status.PENDING);
        responseDTO.setEventName("Ryan Reynolds theme party");
        responseDTO.setSolutionName(null);
        // SolutionName == null -> it's an Event review; else, it's a Solution review
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
