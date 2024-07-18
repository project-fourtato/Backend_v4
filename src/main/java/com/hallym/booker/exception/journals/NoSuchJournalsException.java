package com.hallym.booker.exception.journals;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchJournalsException extends BookerException {
    private static final String MESSAGE = "독서록이 존재하지 않습니다.";
    public NoSuchJournalsException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}