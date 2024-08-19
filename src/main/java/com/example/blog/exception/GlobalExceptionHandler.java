package com.example.blog.exception;

import com.example.blog.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.hibernate.JDBCException;
import org.hibernate.QueryTimeoutException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
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

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse> handleSQLException(SQLException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A database error occurred.");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse> handleDataAccessException(DataAccessException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing data.");
    }

    @ExceptionHandler(JDBCException.class)
    public ResponseEntity<ApiResponse> handleJDBCException(JDBCException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A JDBC error occurred.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Data integrity violation. Please check your input.");
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "A constraint violation occurred.");
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiResponse> handleQueryTimeoutException(QueryTimeoutException ex) {
        return createErrorResponse(HttpStatus.REQUEST_TIMEOUT, "The database query timed out.");
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ApiResponse> handleBadSqlGrammarException(BadSqlGrammarException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error in the SQL syntax.");
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ApiResponse> handleJpaSystemException(JpaSystemException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A JPA error occurred.");
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse> handleTransactionSystemException(TransactionSystemException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A transaction error occurred.");
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ApiResponse> handlePersistenceException(PersistenceException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A persistence error occurred.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "The requested entity was not found.");
    }

    private ResponseEntity<ApiResponse> createErrorResponse(HttpStatus status, String message) {
        ApiResponse error = new ApiResponse(status.value(), false, message);
        // Log the error here
        return new ResponseEntity<>(error, status);
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
            apiResponse = new ApiResponse(status.value(), false, "Forbidden: " + ex.getMessage());
        } else if (ex instanceof java.nio.file.AccessDeniedException) {
            status = HttpStatus.UNAUTHORIZED;
            apiResponse = new ApiResponse(status.value(), false, "Access Denied: " + ex.getMessage());
        } else if (ex instanceof SignatureException) {
            status = HttpStatus.UNAUTHORIZED;
            apiResponse = new ApiResponse(status.value(), false, "JWT Signature is not valid: " + ex.getMessage());
        } else if (ex instanceof ExpiredJwtException) {
            status = HttpStatus.UNAUTHORIZED;
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
