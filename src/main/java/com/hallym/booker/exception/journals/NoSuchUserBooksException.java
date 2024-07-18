package com.hallym.booker.exception.journals;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchUserBooksException extends BookerException {
    private static final String MESSAGE = "저장하지 않은 책입니다.";

    public NoSuchUserBooksException() {
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
