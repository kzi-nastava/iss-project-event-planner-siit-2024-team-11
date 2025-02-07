package org.example.eventy.reviews.controllers;

import jakarta.validation.Valid;
import org.example.eventy.common.models.PagedResponse;
import org.example.eventy.common.models.Status;
import org.example.eventy.events.models.Event;
import org.example.eventy.events.services.EventService;
import org.example.eventy.interactions.dtos.NotificationDTO;
import org.example.eventy.interactions.model.Notification;
import org.example.eventy.interactions.model.NotificationType;
import org.example.eventy.interactions.services.NotificationService;
import org.example.eventy.reviews.dtos.CreateReviewDTO;
import org.example.eventy.reviews.dtos.ReviewDTO;
import org.example.eventy.reviews.dtos.UpdateReviewDTO;
import org.example.eventy.reviews.models.Review;
import org.example.eventy.reviews.services.ReviewService;
import org.example.eventy.solutions.models.Service;
import org.example.eventy.solutions.models.Solution;
import org.example.eventy.solutions.services.SolutionService;
import org.example.eventy.users.models.User;
import org.example.eventy.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
    public ResponseEntity<CreateReviewDTO> createReview(@Valid @RequestBody CreateReviewDTO reviewDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // if there are validation errors, we return a 400 Bad Request response
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST);
        }

        Review review = new Review();
        User grader = userService.get(reviewDTO.getGraderId());
        if (grader == null) {
            return new ResponseEntity<CreateReviewDTO>(reviewDTO, HttpStatus.BAD_REQUEST);
        }
        review.setGrader(grader);

        Event selectedEvent = null;
        Solution selectectedSolution = null;
        if (reviewDTO.getEventId() != null) {
            selectedEvent = eventService.getEvent(reviewDTO.getEventId());
        } else {
            selectectedSolution = solutionService.getSolution(reviewDTO.getSolutionId());
        }
        review.setEvent(selectedEvent);
        review.setSolution(selectectedSolution);

        review.setComment(reviewDTO.getComment());
        review.setGrade(reviewDTO.getGrade());
        review.setStatus(Status.PENDING);
        review.setDeleted(false);

        review = reviewService.saveReview(review);
        if (review == null) {
            return new ResponseEntity<CreateReviewDTO>(HttpStatus.BAD_REQUEST);
        }

        sendNotification(review);
        return new ResponseEntity<CreateReviewDTO>(new CreateReviewDTO(review), HttpStatus.CREATED);
    }

    private void sendNotification(Review review) {
        NotificationType type = null;
        Long redirectionId = null;
        String title = null;
        User owner = null;
        User grader = review.getGrader();
        Integer grade = review.getGrade();
        String message = review.getComment();

        if (review.getEvent() != null) {
            // event
            type = NotificationType.RATING_EVENT;
            redirectionId = review.getEvent().getId();
            title = "\"" + review.getEvent().getName() + "\"";
            owner = review.getEvent().getOrganiser();

        } else {
            // solution
            if (review.getSolution() instanceof Service) {
                type = NotificationType.RATING_SERVICE;
            } else {
                type = NotificationType.RATING_PRODUCT;
            }
            title = "\"" + review.getSolution().getName() + "\"";
            redirectionId = review.getSolution().getId();
            owner = review.getSolution().getProvider();
        }

        LocalDateTime timestamp = LocalDateTime.now();
        Notification notification = new Notification(type, redirectionId, title, message, grader, grade, timestamp);

        notificationService.saveNotification(owner.getId(), notification);
        sendNotificationToWeb(owner.getId(), notification);
        sendNotificationToMobile(owner.getId(), notification);
    }

    private void sendNotificationToWeb(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/web/" + userId, new NotificationDTO(notification));
    }

    private void sendNotificationToMobile(Long userId, Notification notification) {
        messagingTemplate.convertAndSend("/topic/mobile/" + userId, new NotificationDTO(notification));
    }

    // GET "/api/reviews/user/{userId}/solution/{solutionId}"
    @GetMapping(value = "/user/{userId}/solution/{solutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isSolutionReviewedByUser(@PathVariable Long userId, @PathVariable Long solutionId) {
        Boolean exists = reviewService.isSolutionReviewedByUser(userId, solutionId);
        return new ResponseEntity<Boolean>(exists, HttpStatus.OK);
    }

}
