package com.hallym.booker.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InputFieldException extends BookerException {
    protected static final String EMAIL = "email";
    protected static final String NICKNAME = "nickname";
    protected static final String PASSWORD = "password";
    protected static final String JOURNAL_CONTENT = "journalContent";
    protected static final String INTEREST_COUNT = "interestCount";

    private final String field;

    public InputFieldException(final String message, final HttpStatus status, final String field) {
        super(message, status);
        this.field = field;
    }
}
