package org.example.eventy.reviews.controllers;

import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.models.Status;
import org.example.eventy.reviews.dtos.CreateReviewDTO;
import org.example.eventy.reviews.dtos.CreatedReviewDTO;
import org.example.eventy.reviews.dtos.ReviewDTO;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.example.eventy.reviews.models.Review;
import org.example.eventy.reviews.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // GET "/api/reviews/pending"
    @GetMapping(value = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ReviewDTO>> getPendingReviews(Pageable pageable) {
        Page<Review> pendingReviews = reviewService.getPendingReviews(pageable);

        ArrayList<ReviewDTO> pendingReviewsDTO = new ArrayList<>();
        for (Review review : pendingReviews) {
            pendingReviewsDTO.add(new ReviewDTO(review));
        }
        long count = pendingReviews.getTotalElements();

        PagedResponse<ReviewDTO> response = new PagedResponse<>(pendingReviewsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<ReviewDTO>>(response, HttpStatus.OK);
    }

    // GET "/api/reviews/accepted"
    @GetMapping(value = "/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ReviewDTO>> getAcceptedReviews(Pageable pageable) {
        Page<Review> acceptedReviews = reviewService.getAcceptedReviews(pageable);

        ArrayList<ReviewDTO> acceptedReviewsDTO = new ArrayList<>();
        for (Review review : acceptedReviews) {
            acceptedReviewsDTO.add(new ReviewDTO(review));
        }
        long count = acceptedReviews.getTotalElements();

        PagedResponse<ReviewDTO> response = new PagedResponse<>(acceptedReviewsDTO, (int) Math.ceil((double) count / pageable.getPageSize()), count);
        return new ResponseEntity<PagedResponse<ReviewDTO>>(response, HttpStatus.OK);
    }

    // GET "/api/reviews/5"
    @GetMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);

        if (review != null) {
            return new ResponseEntity<ReviewDTO>(new ReviewDTO(review), HttpStatus.OK);
        }

        return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
    }

    /*
    {
        "id": 5,
        "comment": "Updated review comment",
        "grade": 4,
        "status": "PENDING"
    }
    */
    // PUT "/api/reviews"
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> updateReview(@RequestBody UpdateReviewDTO updateReviewDTO) {
        Review review = reviewService.getReview(updateReviewDTO.getId());
        if(review == null) {
            return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
        }

        review = reviewService.updateReview(review, updateReviewDTO);
        if(review == null) {
            return new ResponseEntity<ReviewDTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ReviewDTO>(new ReviewDTO(review), HttpStatus.CREATED);
    }

    // DELETE "/api/reviews/5"
    @DeleteMapping(value ="/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        if(review == null) {
            return new ResponseEntity<ReviewDTO>(HttpStatus.NOT_FOUND);
        }

        review = reviewService.deleteReview(review);
        if(review == null) {
            return new ResponseEntity<ReviewDTO>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<ReviewDTO>(new ReviewDTO(review), HttpStatus.NO_CONTENT);
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
