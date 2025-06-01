package com.example.Bookstore.exception;

import com.example.Bookstore.payload.ApiErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle Book Not Found Exception
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFound(final BookNotFoundException ex, final WebRequest request) {
        try {
            final var apiError = new ApiErrorDTO(
                    LocalDateTime.now(),
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", ""),
                    null
            );
            log.debug("Exception: {}", apiError);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
        } catch (final Exception e) {
            log.debug("Exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Handle validation errors (e.g., @Valid fails)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        try {
            final var validationErrors = ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            final var apiError = new ApiErrorDTO(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "Validation failed",
                    request.getDescription(false).replace("uri=", ""),
                    validationErrors
            );
            log.debug("Exception: {}", apiError);
            return ResponseEntity.badRequest().body(apiError);
        } catch (final Exception e) {
            log.debug("Exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Fallback for any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(final Exception ex, final WebRequest request) {
        try {
            final var apiError = new ApiErrorDTO(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", ""),
                    null
            );
            log.debug("Exception: {}", apiError);
            return ResponseEntity.internalServerError().body(apiError);
        } catch (final Exception e) {
            log.debug("Exception: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
