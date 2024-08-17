package com.hallym.booker.dto.UserBooks;

import com.hallym.booker.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingProfile {
    String loginId;
    String userimageUrl;
    String nickname;

    public static ReadingProfile of(Profile profile) {
        return new ReadingProfile(profile.getLogin().getLoginUid(),
                profile.getUserimageUrl(),
                profile.getNickname());
    }
}
