package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.BookDetails;
import com.hallym.booker.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ReadingWithAllProfileList {
    List<ReadingWithProfile> result;

    public static ReadingWithAllProfileList from(Map<BookDetails, List<Profile>> map) {

        List<ReadingWithProfile> readingWithProfileStream = map.entrySet().stream()
                .map(m -> ReadingWithProfile.from(m.getKey(), m.getValue())).toList();

        return new ReadingWithAllProfileList(readingWithProfileStream);
    }

}
