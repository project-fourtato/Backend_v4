package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import com.hallym.booker.domain.UserBooks;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReadingWithAllProfileList {
    List<ReadingWithProfile> result;

    public static ReadingWithAllProfileList from(Map<BookDetails, ReadingProfileWithBookUid> map) {
        List<ReadingWithProfile> readingWithProfileStream = map.entrySet().stream()
                .map(m -> ReadingWithProfile.from(m.getKey(), m.getValue())).toList();

        return new ReadingWithAllProfileList(readingWithProfileStream);
    }
}
