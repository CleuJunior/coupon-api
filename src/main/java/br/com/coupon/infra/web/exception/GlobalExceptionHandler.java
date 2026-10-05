package br.com.coupon.infra.web.exception;

import br.com.coupon.domain.exception.CouponAlreadyDeletedException;
import br.com.coupon.domain.exception.CouponCreationException;
import br.com.coupon.domain.exception.CouponNotFoundException;
import br.com.coupon.infra.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponCreationException.class)
    public ResponseEntity<ErrorResponse> handleCouponCreation(CouponCreationException ex) {
        HttpStatus status = BAD_REQUEST;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCouponNotFound(CouponNotFoundException ex) {
        HttpStatus status = NOT_FOUND;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(CouponAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponse> handleCouponAlreadyDeleted(CouponAlreadyDeletedException ex) {
        HttpStatus status = CONFLICT;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        HttpStatus status = BAD_REQUEST;
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        HttpStatus status = INTERNAL_SERVER_ERROR;

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "Unexpected error occurred"
        );

        return ResponseEntity.status(status).body(body);
    }
}
