package com.hallym.booker.dto.UserBooks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingProfileWithBookUid {
    List<ReadingProfile> readingProfile;
    Long bookUid;

    public static ReadingProfileWithBookUid of(List<ReadingProfile> readingProfile, Long bookUid) {
        return new ReadingProfileWithBookUid(readingProfile, bookUid);
    }
}
