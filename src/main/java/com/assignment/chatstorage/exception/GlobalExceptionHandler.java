package com.assignment.chatstorage.exception;

import com.assignment.chatstorage.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {

        int status = switch (ex) {
            case SessionNotFoundException s -> HttpStatus.NOT_FOUND.value();
            case MessageNotFoundException m -> HttpStatus.NOT_FOUND.value();
            case MethodArgumentNotValidException v -> HttpStatus.BAD_REQUEST.value();
            case MethodArgumentTypeMismatchException t -> HttpStatus.BAD_REQUEST.value();
            case org.springframework.web.server.ResponseStatusException r -> r.getStatusCode().value();
            default -> HttpStatus.INTERNAL_SERVER_ERROR.value();
        };

        String message = switch (ex) {
            case SessionNotFoundException s -> s.getMessage();
            case MessageNotFoundException m -> m.getMessage();
            case MethodArgumentNotValidException v -> String.join("; ",
                    v.getBindingResult().getFieldErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList());
            case MethodArgumentTypeMismatchException t ->
                    "Invalid type for parameter '" + t.getName() + "'";
            case org.springframework.web.server.ResponseStatusException r -> r.getReason();
            default -> "Something went wrong. Please contact support.";
        };

        String traceId = MDC.get("traceId");

        if (status >= 500) {
            log.error("Exception occurred. traceId={}, status={}, message={}",
                    traceId, status, message, ex);
        } else {
            log.warn("Client error. traceId={}, status={}, message={}",
                    traceId, status, message);
        }
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status, message));
    }
}
