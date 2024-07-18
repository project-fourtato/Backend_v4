package com.hallym.booker.exception.directmessage;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchMessageException extends BookerException {
    private static final String MESSAGE = "존재하지 않는 메세지입니다.";

    public NoSuchMessageException() {
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
