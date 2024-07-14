package com.hallym.booker.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BookerException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "일시적인 오류입니다. 잠시 후에 다시 시도해 주세요.";

    private final HttpStatus status;

    public BookerException() {
        super(DEFAULT_MESSAGE);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BookerException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BookerException(final String message, final Throwable cause, final HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

}
