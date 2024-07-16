package com.hallym.booker.exception.profile;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchLoginException extends BookerException {
    private static final String MESSAGE = "login 데이터가 존재하지 않는 회원입니다.";

    public NoSuchLoginException() {
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
