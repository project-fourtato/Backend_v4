package com.hallym.booker.dto;

import com.hallym.booker.domain.UserBooks;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadingBookResponse {
    Long bookUid;
    String isbn;
    String coverImageUrl;
    Integer readStatus;
    Integer saleStatus;

    public static ReadingBookResponse of(UserBooks userBooks) {
        return new ReadingBookResponse(
                userBooks.getBookUid(),
                userBooks.getBookDetails().getIsbn(),
                userBooks.getBookDetails().getCoverImageUrl(),
                userBooks.getReadStatus(),
                userBooks.getSaleStatus()
        );
    }
}
