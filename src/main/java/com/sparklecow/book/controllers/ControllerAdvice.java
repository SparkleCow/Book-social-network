package com.sparklecow.book.controllers;

import com.sparklecow.book.exceptions.*;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException e){
        return ResponseEntity
                .status(USER_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(USER_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(USER_NOT_FOUND.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RoleNameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoleNameNotFoundException(RoleNameNotFoundException e){
        return ResponseEntity
                .status(ROLE_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ROLE_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(ROLE_NOT_FOUND.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTokenNotFoundException(TokenNotFoundException e){
        return ResponseEntity
                .status(TOKEN_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(TOKEN_NOT_FOUND.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ValidatedTokenException.class)
    public ResponseEntity<ExceptionResponse> handleValidatedTokenException(ValidatedTokenException e){
        return ResponseEntity
                .status(TOKEN_ALREADY_VALIDATE.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_ALREADY_VALIDATE.getErrorCode())
                        .businessErrorDescription(TOKEN_ALREADY_VALIDATE.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredTokenException(ExpiredTokenException e){
        return ResponseEntity
                .status(TOKEN_EXPIRED.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_EXPIRED.getErrorCode())
                        .businessErrorDescription(TOKEN_EXPIRED.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity
                .status(BAD_CREDENTIALS.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getErrorCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalOperationException(IllegalOperationException e){
        return ResponseEntity
                .status(ILLEGAL_OPERATION.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ILLEGAL_OPERATION.getErrorCode())
                        .businessErrorDescription(ILLEGAL_OPERATION.getMessage())
                        .message(e.getMessage())
                        .build());
    }
}
