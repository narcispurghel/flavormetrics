package com.flavormetrics.api.exception;

import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flavormetrics.api.model.response.ApiErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatchException(TypeMismatchException e) {
        ApiErrorResponse response = new ApiErrorResponse(401, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ApiErrorResponse response = new ApiErrorResponse(401, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            EmailInUseException.class,
            ProfileExistsException.class,
            MaximumNumberOfRatingException.class
    })
    public ResponseEntity<Map<String, String>> handleConflictExceptions(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", e.getMessage(), "code", "409"));
    }

    @ExceptionHandler(value = {RecipeNotFoundException.class, ProfileNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundExceptions(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage(), "code", "404"));
    }

    @ExceptionHandler(value = {UnAuthorizedException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleAuthExceptions(UnAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage(), "code", "401"));
    }

}
