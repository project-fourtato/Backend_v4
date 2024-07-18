package com.hallym.booker.exception.profile;

import com.hallym.booker.exception.InputFieldException;
import org.springframework.http.HttpStatus;

public class TooManyInterestException extends InputFieldException {
    private static final String MESSAGE = "관심사는 최대 5개까지 선택이 가능합니다.";

    public TooManyInterestException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST, INTEREST_COUNT);
    }
}
