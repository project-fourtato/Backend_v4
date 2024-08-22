package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.BookDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReadingWithUser {
    Long bookUid;
    String isbn;
    String bookTitle;
    String author;
    String publisher;
    String coverImageUrl;
    List<ReadingProfile> profileList;

    public static ReadingWithUser from(BookDetails bookDetails, ReadingProfileWithBookUid readingProfileWithBookUid) {
        return new ReadingWithUser(
                readingProfileWithBookUid.getBookUid(),
                bookDetails.getIsbn(),
                bookDetails.getBookTitle(),
                bookDetails.getAuthor(),
                bookDetails.getPublisher(),
                bookDetails.getCoverImageUrl(),
                readingProfileWithBookUid.getReadingProfile());
    }
}
