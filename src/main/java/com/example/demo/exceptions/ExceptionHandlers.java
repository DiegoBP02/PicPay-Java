package com.example.demo.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> Exception(Exception e, HttpServletRequest request) {
        String error = "Server error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<StandardError> SignatureVerificationException
            (SignatureVerificationException e, HttpServletRequest request) {
        String error = "Invalid token signature";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<StandardError> JWTDecodeException
            (JWTDecodeException e, HttpServletRequest request) {
        String error = "Error decoding JWT token";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<StandardError> TokenExpiredException
            (TokenExpiredException e, HttpServletRequest request) {
        String error = "Token expired";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(UniqueConstraintViolationError.class)
    public ResponseEntity<StandardError> UniqueConstraintViolationError
            (UniqueConstraintViolationError e, HttpServletRequest request) {
        String error = "Duplicate entry found. Please provide a unique value";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> MethodArgumentNotValidException
            (MethodArgumentNotValidException e, HttpServletRequest request) {
        String error = "Invalid arguments";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        final List<String> errors = new ArrayList<>();
        for (final FieldError err : e.getBindingResult().getFieldErrors()) {
            errors.add(err.getField() + ": " + err.getDefaultMessage());
        }

        StandardError err = new StandardError(Instant.now(), status.value(), error,
                errors.toString(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFoundException
            (ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<StandardError> insufficientBalanceException
            (InsufficientBalanceException e, HttpServletRequest request) {
        String error = "Insufficient balance";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(TransferNotAuthorizedException.class)
    public ResponseEntity<StandardError> transferNotAuthorizedException
            (TransferNotAuthorizedException e, HttpServletRequest request) {
        String error = "Transfer not authorized";
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<StandardError> apiErrorException
            (ApiErrorException e, HttpServletRequest request) {
        String error = "API Error";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> httpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        String error = "Malformed request body";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        String error = "Access denied";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity<StandardError> selfTransferException(
            SelfTransferException e, HttpServletRequest request) {
        String error = "Self transfer error";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}
