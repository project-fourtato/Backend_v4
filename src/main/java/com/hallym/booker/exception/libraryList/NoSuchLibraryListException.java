package com.hallym.booker.exception.libraryList;

import com.hallym.booker.exception.BookerException;
import org.springframework.http.HttpStatus;

public class NoSuchLibraryListException extends BookerException {
    private static final String MESSAGE = "존재하지 않는 도서관입니다.";
    public NoSuchLibraryListException(){
        super(MESSAGE, HttpStatus.NOT_FOUND);
    }
}