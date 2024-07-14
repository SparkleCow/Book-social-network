package com.sparklecow.book.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private int businessErrorCode;
    private String businessErrorDescription;
    private Set<String> validationErrors;
    private Map<String, String> errorDetails;
}
