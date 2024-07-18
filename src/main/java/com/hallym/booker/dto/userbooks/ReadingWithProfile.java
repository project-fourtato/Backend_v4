package com.hallym.booker.dto.userbooks;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReadingWithProfile {
    String isbn;
    String bookTitle;
    String author;
    String publisher;
    String coverImageUrl;
    List<ReadingProfile> profileList;

    public static ReadingWithProfile from(BookDetails bookDetails, List<Profile> profileList) {
        return new ReadingWithProfile(bookDetails.getIsbn(),
                bookDetails.getBookTitle(),
                bookDetails.getAuthor(),
                bookDetails.getPublisher(),
                bookDetails.getCoverImageUrl(),
                profileList.stream()
                        .map(ReadingProfile::of).toList()
                );
    }
}
