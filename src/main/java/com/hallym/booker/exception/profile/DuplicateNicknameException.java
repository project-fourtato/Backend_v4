package com.hallym.booker.exception.profile;

import com.hallym.booker.exception.BookerException;
import com.hallym.booker.exception.InputFieldException;
import org.springframework.http.HttpStatus;

public class DuplicateNicknameException extends InputFieldException {
    private static final String MESSAGE = "이미 사용 중인 닉네임입니다.";

    public DuplicateNicknameException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST, NICKNAME);
    }
}
