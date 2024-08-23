package com.hallym.booker.exception.userBooks;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchAladinApiIsbnSearchResult extends BookerException {
    private static final String MESSAGE = "알라딘에서 조회되지 않는 책입니다.";

    public NoSuchAladinApiIsbnSearchResult(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}
