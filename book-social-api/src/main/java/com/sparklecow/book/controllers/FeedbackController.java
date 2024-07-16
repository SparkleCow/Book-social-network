package com.sparklecow.book.controllers;

import com.sparklecow.book.dto.feedback.FeedbackRequestDto;
import com.sparklecow.book.dto.feedback.FeedbackResponseDto;
import com.sparklecow.book.exceptions.IllegalOperationException;
import com.sparklecow.book.exceptions.OperationNotPermittedException;
import com.sparklecow.book.models.PageResponse;
import com.sparklecow.book.services.feedback.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(@Valid @RequestBody FeedbackRequestDto feedbackRequestDto, @Valid Authentication connectedUser) throws IllegalOperationException, OperationNotPermittedException {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedbackRequestDto, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponseDto>> getAllFeedbacksByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }
}
