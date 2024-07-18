package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadingProfile {
    Long profileId;
    String userimageUrl;
    String nickname;

    public static ReadingProfile of(Profile profile) {
        return new ReadingProfile(profile.getProfileUid(),
                profile.getUserimageUrl(),
                profile.getNickname());
    }
}
