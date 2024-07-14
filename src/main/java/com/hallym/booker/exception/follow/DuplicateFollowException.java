package com.hallym.booker.exception.follow;


import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class DuplicateFollowException extends BookerException {
    private static final String MESSAGE = "이미 팔로잉하고 있는 회원입니다.";
    public DuplicateFollowException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
