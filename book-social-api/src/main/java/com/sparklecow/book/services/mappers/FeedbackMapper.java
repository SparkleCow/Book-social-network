package com.sparklecow.book.services.mappers;

import com.sparklecow.book.dto.feedback.FeedbackRequestDto;
import com.sparklecow.book.dto.feedback.FeedbackResponseDto;
import com.sparklecow.book.entities.book.Book;
import com.sparklecow.book.entities.feedback.Feedback;

public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequestDto feedbackRequestDto, Book book) {
        return Feedback.builder()
                .note(feedbackRequestDto.note())
                .comment(feedbackRequestDto.comment())
                .book(book)
                .build();
    }

    public FeedbackResponseDto toFeedbackResponseDto(Feedback feedback, Integer userId) {
        return FeedbackResponseDto.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(feedback.getCreatedBy().equals(userId))
                .build();
    }
}
