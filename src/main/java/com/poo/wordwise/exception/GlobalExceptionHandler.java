package com.poo.wordwise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 401 - No autenticado (token inválido, expirado o faltante)
     */
    @ExceptionHandler(WordWiseUnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            WordWiseUnauthorizedException ex,
            WebRequest request
    ) {
        Map<String, Object> body = createErrorBody(
                ex.getMessage(),
                "Unauthorized",
                request
        );
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 400 - Validaciones (datos incorrectos, formatos inválidos, etc.)
     */
    @ExceptionHandler(WordWiseValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            WordWiseValidationException ex,
            WebRequest request
    ) {
        Map<String, Object> body = createErrorBody(
                ex.getMessage(),
                "Validation Error",
                request
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 404 - Recurso no encontrado
     */
    @ExceptionHandler(WordWiseDataNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            WordWiseDataNotFoundException ex,
            WebRequest request
    ) {
        Map<String, Object> body = createErrorBody(
                ex.getMessage(),
                "Not Found",
                request
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * 400 - IllegalArgumentException (validaciones de Java)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        Map<String, Object> body = createErrorBody(
                ex.getMessage(),
                "Bad Request",
                request
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 500 - Error de IO (archivos, Cloudinary, etc.)
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(
            IOException ex,
            WebRequest request
    ) {
        Map<String, Object> body = createErrorBody(
                "Error al procesar archivo: " + ex.getMessage(),
                "Internal Server Error",
                request
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 500 - Cualquier otra excepción no contemplada
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex,
            WebRequest request
    ) {
        Map<String, Object> body = createErrorBody(
                "Error interno del servidor: " + ex.getMessage(),
                "Internal Server Error",
                request
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> createErrorBody(String message, String error, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("success", false);
        body.put("error", error);
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }
}
