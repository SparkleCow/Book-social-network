package com.sparklecow.book.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponseDto {
    private Double note;
    private String comment;
    private boolean ownFeedback;
}
