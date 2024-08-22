package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.BookDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReadingWithAllUserList {
    List<ReadingWithUser> result;
    public static ReadingWithAllUserList from(Map<BookDetails, ReadingProfileWithBookUid> map) {
        List<ReadingWithUser> readingWithUserStream = map.entrySet().stream()
                .map(m -> ReadingWithUser.from(m.getKey(), m.getValue())).toList();

        return new ReadingWithAllUserList(readingWithUserStream);
    }
}
