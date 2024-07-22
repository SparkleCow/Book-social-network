package com.sparklecow.book.dto.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequestDto(
        @NotNull(message = "Title is required")
        @NotEmpty(message = "Title cannot be empty")
        String  title,
        @NotNull(message = "Author name is required")
        @NotEmpty(message = "Author name cannot be empty")
        String authorName,
        @NotNull(message = "ISBN cat not be null")
        @NotEmpty(message = "ISBN cat not be empty")
        String isbn,
        @NotNull(message = "Synopsis is required")
        @NotEmpty(message = "Synopsis cannot be empty")
        String synopsis,
        boolean shareable
) {
}
