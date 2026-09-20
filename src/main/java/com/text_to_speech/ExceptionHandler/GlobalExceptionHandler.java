package com.text_to_speech.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // VALIDATION ERRORS
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid request.");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
    }

    // ==========================================
    // INVALID REQUEST / BUSINESS VALIDATION
    // ==========================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage() != null
                        ? ex.getMessage()
                        : "Invalid request."
        );
    }

    // ==========================================
    // TTS PROVIDER HTTP ERRORS
    // ==========================================

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleTtsProviderError(
            RestClientResponseException ex
    ) {

        int statusCode = ex.getStatusCode().value();

        if (statusCode == 401 || statusCode == 403) {

            return buildResponse(
                    HttpStatus.BAD_GATEWAY,
                    "TTS provider authentication failed. Check the API key."
            );
        }

        if (statusCode == 429) {

            return buildResponse(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "TTS provider rate limit reached. Please try again later."
            );
        }

        if (statusCode >= 500) {

            return buildResponse(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "TTS provider is currently unavailable."
            );
        }

        return buildResponse(
                HttpStatus.BAD_GATEWAY,
                "TTS provider rejected the request."
        );
    }

    // ==========================================
    // NETWORK / CONNECTION ERRORS
    // ==========================================

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleNetworkError(
            ResourceAccessException ex
    ) {

        return buildResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Unable to connect to the TTS provider."
        );
    }

    // ==========================================
    // ALL OTHER SERVER ERRORS
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex
    ) {

        ex.printStackTrace();

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong on the server."
        );
    }

    // ==========================================
    // COMMON RESPONSE
    // ==========================================

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message
    ) {

        Map<String, Object> response = new HashMap<>();

        response.put("success", false);
        response.put("status", status.value());
        response.put("message", message);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}