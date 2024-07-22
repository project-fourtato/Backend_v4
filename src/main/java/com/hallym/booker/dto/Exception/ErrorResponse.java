package com.hallym.booker.dto.Exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    String message;

    private static final String SERVER_ERROR_MESSAGE = "일시적인 접속 오류입니다. 잠시 후 다시 시도해 주시기 바랍니다.";

    ErrorResponse(final String message) {
        this.message = message;
    }

    public static ErrorResponse from(final RuntimeException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    public static ErrorResponse from(final ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().iterator().next().getMessage();
        return new ErrorResponse(message);
    }

    public static ErrorResponse internalServerError() {
        return new ErrorResponse(SERVER_ERROR_MESSAGE);
    }

}
