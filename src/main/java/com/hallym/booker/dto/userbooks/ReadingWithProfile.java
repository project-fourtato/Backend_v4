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

    public static ReadingWithProfile from(UserBooks userBooks, ReadingProfileWithBookUid readingProfileWithBookUid) {
        return new ReadingWithProfile(
                userBooks.getBookUid(),
                userBooks.getBookDetails().getIsbn(),
                userBooks.getBookDetails().getBookTitle(),
                userBooks.getBookDetails().getAuthor(),
                userBooks.getBookDetails().getPublisher(),
                userBooks.getBookDetails().getCoverImageUrl(),
                readingProfileWithBookUid.getReadingProfile());
    }
}
