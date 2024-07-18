package com.hallym.booker.dto.userbooks;

import com.hallym.booker.domain.UserBooks;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReadingAllBooksListResponse {
    List<ReadingBookResponse> result;

    public static ReadingAllBooksListResponse from(List<UserBooks> userBooksList) {
        List<ReadingBookResponse> readingBookResponseList = userBooksList.stream()
                .map(ReadingBookResponse::of).toList();
        return new ReadingAllBooksListResponse(readingBookResponseList);
    }

}
