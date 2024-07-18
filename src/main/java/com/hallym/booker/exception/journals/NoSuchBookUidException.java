package com.hallym.booker.exception.journals;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchBookUidException extends BookerException {
    private static final String MESSAGE = "주어진 BookUid에 해당하는 독서록이 존재하지 않습니다.";
    public NoSuchBookUidException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}