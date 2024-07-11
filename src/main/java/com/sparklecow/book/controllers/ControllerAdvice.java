package com.sparklecow.book.controllers;

import com.sparklecow.book.exceptions.ExceptionResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparklecow.book.models.BusinessErrorCodes.*;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException e){
        return ResponseEntity
                .status(MESSAGE_ERROR.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(MESSAGE_ERROR.getErrorCode())
                        .businessErrorDescription(MESSAGE_ERROR.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException e){
        return ResponseEntity
                .status(ACCOUNT_DISABLED.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getErrorCode())
                        .businessErrorDescription(ACCOUNT_DISABLED.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e){
        return ResponseEntity
                .status(BAD_CREDENTIALS.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getErrorCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getMessage())
                        .message(e.getMessage())
                        .build());
    }
}
