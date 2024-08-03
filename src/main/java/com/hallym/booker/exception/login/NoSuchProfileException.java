package com.hallym.booker.exception.login;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchProfileException extends BookerException {
    private static final String MESSAGE = "회원정보를 입력하지 않은 회원입니다.";
    public NoSuchProfileException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
