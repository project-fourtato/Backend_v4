package com.hallym.booker.exception.userBooks;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchUserBooksException extends BookerException {
    private static final String MESSAGE = "존재하지 않는 책입니다.";

    public NoSuchUserBooksException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }

}