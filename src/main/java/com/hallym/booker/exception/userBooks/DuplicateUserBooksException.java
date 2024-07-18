package com.hallym.booker.exception.userBooks;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class DuplicateUserBooksException extends BookerException {
    private static final String MESSAGE = "이미 존재하는 책입니다.";

    public DuplicateUserBooksException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}