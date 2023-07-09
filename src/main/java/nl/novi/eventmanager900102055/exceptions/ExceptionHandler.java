package nl.novi.eventmanager900102055.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
//
//        @ExceptionHandler(ResourceNotFoundException.class)
//        public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
//            // Create a custom error response or DTO
//            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//        }
//
//        @ExceptionHandler(NameDuplicateException.class)
//        public ResponseEntity<Object> handleNameDuplicateException(NameDuplicateException ex) {
//            // Create a custom error response or DTO
//            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//        }
//
//        // Add more exception handler methods as needed for other custom exceptions
//
//        // Catch generic exceptions if needed
//        @ExceptionHandler(Exception.class)
//        public ResponseEntity<Object> handleGenericException(Exception ex) {
//            // Create a custom error response or DTO
//            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
    }
