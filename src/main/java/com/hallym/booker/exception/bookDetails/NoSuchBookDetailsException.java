package com.hallym.booker.exception.bookDetails;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchBookDetailsException extends BookerException {
    private static final String MESSAGE = "해당 책의 상세정보가 존재하지 않습니다.";

    public NoSuchBookDetailsException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}