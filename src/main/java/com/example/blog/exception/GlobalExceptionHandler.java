package com.example.blog.exception;

import com.example.blog.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        ApiResponse apiResponse = new ApiResponse(404, false, exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> apiResponseExceptionHandler(ApiException exception) {
        ApiResponse apiResponse = new ApiResponse(400, false, exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, String> response = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(
                (error -> {
                    String fieldError = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    response.put(fieldError, message);
                })
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> exceptionHandler(Exception ex) {
        ApiResponse apiResponse;
        HttpStatus status;

        if (ex instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            apiResponse = new ApiResponse(status.value(), false, "Authentication Failure: " + ex.getMessage());
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            apiResponse = new ApiResponse(status.value(), false, "Access Denied: " + ex.getMessage());
        } else if (ex instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            apiResponse = new ApiResponse(status.value(), false, "JWT Signature is not valid: " + ex.getMessage());
        } else if (ex instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            apiResponse = new ApiResponse(status.value(), false, "JWT Token is expired: " + ex.getMessage());
        } else if (ex instanceof UsernameNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            apiResponse = new ApiResponse(status.value(), false, "User not found: " + ex.getMessage());
        } else if (ex instanceof ResourceNotFoundException) {
            status = HttpStatus.FORBIDDEN;
            apiResponse = new ApiResponse(status.value(), false, "Token is not provided in the header: " + ex.getMessage());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse = new ApiResponse(status.value(), false, "An unexpected error occurred: " + ex.getMessage());
        }

        return new ResponseEntity<>(apiResponse, status);
    }


}
