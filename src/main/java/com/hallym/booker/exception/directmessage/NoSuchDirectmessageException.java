package com.hallym.booker.exception.directmessage;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchDirectmessageException extends BookerException {
    private static final String MESSAGE = "쪽지 목록이 존재하지 않습니다.";

    public NoSuchDirectmessageException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
