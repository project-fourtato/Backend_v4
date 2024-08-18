package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.print.Book;
import java.util.List;

@Data
@AllArgsConstructor
public class ReadingWithProfile {
    Long bookUid;
    String isbn;
    String bookTitle;
    String author;
    String publisher;
    String coverImageUrl;
    List<ReadingProfile> profileList;

    public static ReadingWithProfile from(BookDetails bookDetails, ReadingProfileWithBookUid readingProfileWithBookUid) {
        return new ReadingWithProfile(
                readingProfileWithBookUid.getBookUid(),
                bookDetails.getIsbn(),
                bookDetails.getBookTitle(),
                bookDetails.getAuthor(),
                bookDetails.getPublisher(),
                bookDetails.getCoverImageUrl(),
                readingProfileWithBookUid.getReadingProfile());
    }
}
