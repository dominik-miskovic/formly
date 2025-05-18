package com.dominikmiskovic.forumly.exception; // Make sure this matches your package structure

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Option 1: If you want Spring MVC to automatically map this exception to an HTTP status code
// This is very convenient if you don't want to write a specific @ExceptionHandler for it.
@ResponseStatus(value = HttpStatus.NOT_FOUND) // Sets the HTTP response status to 404
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Optional: If you want to provide more context, like resource name and field value
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}