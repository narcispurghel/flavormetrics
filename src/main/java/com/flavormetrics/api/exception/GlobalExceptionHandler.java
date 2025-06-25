package com.flavormetrics.api.exception;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ApiErrorResponse> handleTypeMismatchException(TypeMismatchException e, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                401,
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(), request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                401,
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(), request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            EmailInUseException.class,
            ProfileExistsException.class,
            MaximumNumberOfRatingException.class
    })
    public ResponseEntity<ApiErrorResponse> handleConflictExceptions(RuntimeException e, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                409,
                HttpStatus.CONFLICT.name(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(response.code()).body(response);
    }

    @ExceptionHandler(value = {RecipeNotFoundException.class, ProfileNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundExceptions(RuntimeException e, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                409,
                HttpStatus.NOT_FOUND.name(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(response.code()).body(response);
    }

    @ExceptionHandler(value = {UnAuthorizedException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleAuthExceptions(UnAuthorizedException e, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                401,
                HttpStatus.UNAUTHORIZED.name(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(response.code()).body(response);
    }

    @ExceptionHandler(value = InvalidImageException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidFileException(InvalidImageException e, HttpServletRequest request) {
        var response = new ApiErrorResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(response.code()).body(response);
    }

}
