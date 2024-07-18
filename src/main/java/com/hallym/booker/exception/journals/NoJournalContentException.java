package com.hallym.booker.exception.journals;

import com.hallym.booker.exception.InputFieldException;
import org.springframework.http.HttpStatus;

public class NoJournalContentException extends InputFieldException {
    private static final String MESSAGE = "독서록의 제목, 내용을 입력해 주셔야 합니다.";

    public NoJournalContentException() {
        super(MESSAGE, HttpStatus.BAD_REQUEST, JOURNAL_CONTENT);
    }
}
