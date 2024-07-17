package com.hallym.booker.dto.Profile;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class SameInterestProfileResponse {
    String nickname;
    String imageUrl;
    String imageName;
    String usermessage;
    List<String> interests;

    public static SameInterestProfileResponse from(Profile profile) {
        return new SameInterestProfileResponse(
                profile.getNickname(),
                profile.getUserimageUrl(),
                profile.getUserimageName(),
                profile.getUsermessage(),
                profile.getInterests().stream().map(SameInterestProfileResponse::getInterest)
                        .collect(Collectors.toList())
        );
    }

    public static String getInterest(Interests interests) {
        return interests.getInterestName();
    }
}
