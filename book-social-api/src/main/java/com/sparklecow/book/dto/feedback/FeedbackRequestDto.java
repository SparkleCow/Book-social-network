package com.sparklecow.book.dto.feedback;

import jakarta.validation.constraints.*;


public record FeedbackRequestDto(
        @Positive(message = "Note has to be a positive number")
        @Min(value = 0, message = "Note has to be higher than 0")
        @Max(value = 5, message = "Note has to be lower than 5")
        Double note,
        @NotNull(message = "Comment cannot be null")
        @NotEmpty(message = "Comment cannot be empty")
        @NotBlank(message = "Comment cannot be blank")
        String comment,
        @NotNull(message = "BookId cannot be null")
        Integer bookId) {

}
