package com.hallym.booker.exception.profile;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class DuplicateProfileException extends BookerException {
    private static final String MESSAGE = "이미 존재하는 회원입니다.";

    public DuplicateProfileException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}