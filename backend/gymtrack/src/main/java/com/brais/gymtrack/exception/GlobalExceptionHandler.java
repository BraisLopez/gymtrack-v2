package com.brais.gymtrack.exception;


import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.brais.gymtrack.exception.customExceptions.BadRequestException;
import com.brais.gymtrack.exception.customExceptions.EmailAlreadyExistsException;
import com.brais.gymtrack.exception.customExceptions.InvalidCredentialsException;
import com.brais.gymtrack.exception.customExceptions.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Centralizes exception handling for the REST controllers.
 *
 * This class converts application exceptions into clean HTTP responses.
 * It handles errors thrown from controllers and services.
 * 
 * Security errors that happen inside filters, such as invalid JWT tokens,
 * are handled separately in the security layer.
 */


@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles validation errores triggered by @Valid
     * 
     * Invalid email format
     * blank password
     * null required fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ){
        Map<String,String> fieldErrors = new HashMap();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())  
        );

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", "Validation failed");
        body.put("path", request.getRequestURI());
        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Handles attempts to create a user with an email that akready exists.
     * Returns 409 Conflict because the request conflicts with the current database state.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
        EmailAlreadyExistsException ex,
        HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.getReasonPhrase(), 
            ex.getMessage(), 
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Handles user lookup failures.
     * Returns 404 Not Found when the requested user does not exist.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
        UserNotFoundException ex,
        HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(), 
            ex.getMessage(), 
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handlesfailed login attempts.
     * The response does not reveal whether the email or password was wrong.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
        InvalidCredentialsException ex,
        HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase(), 
            ex.getMessage(), 
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handles authorization failures
     * like trying to create an ADMIN user from the admin user creation endpoint
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
        BadRequestException ex,
        HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), 
            ex.getMessage(), 
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Handles authorization failures.
     * This happens when teh user is authenticated but does not have the required role.
     * 
     * Client trying to acces an ADMIN-only endpoint.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccesDeniedException(
        AccessDeniedException ex,
        HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN.getReasonPhrase(), 
            "Access denied", 
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
