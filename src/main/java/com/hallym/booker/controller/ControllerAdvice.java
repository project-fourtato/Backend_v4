package com.hallym.booker.controller;

import com.amazonaws.Response;
import com.hallym.booker.dto.Exception.ErrorResponse;
import com.hallym.booker.dto.Exception.InputFieldErrorResponse;
import com.hallym.booker.exception.BookerException;
import com.hallym.booker.exception.InputFieldException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.MDC;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    private static final String MESSAGE_FORMAT = "%s (traceId: %s)";
    private static final String TRACE_ID_KEY = "traceId";

    @ExceptionHandler(InputFieldException.class)
    public ResponseEntity<InputFieldErrorResponse> inputFieldException(final InputFieldException exception) {
        logInfo(exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(InputFieldErrorResponse.from(exception));
    }

    @ExceptionHandler(BookerException.class)
    public ResponseEntity<ErrorResponse> bookerExceptionHandler(final BookerException exception) {
        logInfo(exception);
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //@Valid exception handler
    public ResponseEntity<InputFieldErrorResponse> invalidArgumentHandler(final MethodArgumentNotValidException exception) {
        logInfo(exception);
        return ResponseEntity.badRequest()
                .body(InputFieldErrorResponse.from(exception));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> dataViolationHandler(final ConstraintViolationException exception) {
        logInfo(exception);
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> invalidDataAccessHandler(final DataAccessException exception) {
        logWarn(exception);
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.internalServerError());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> invalidDataAccessHandler(final Exception exception) {
        logWarn(exception);
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.internalServerError());
    }

    private void logInfo(Exception exception) {
        String logMessage = String.format(MESSAGE_FORMAT, exception.getMessage(), getTraceId());
        log.info(logMessage);
    }

    private void logWarn(Exception exception) {
        String logMessage = String.format(MESSAGE_FORMAT, exception.getMessage(), getTraceId());
        log.warn(logMessage);
    }

    private Object getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }
}
