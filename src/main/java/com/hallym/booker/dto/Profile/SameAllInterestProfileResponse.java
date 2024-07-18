package com.hallym.booker.dto.Profile;

import com.hallym.booker.domain.Interests;
import com.hallym.booker.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SameAllInterestProfileResponse {
    List<SameInterestProfileResponse> result;

    public static SameAllInterestProfileResponse from(List<Profile> profiles) {
        List<SameInterestProfileResponse> sameInterestProfile = profiles.stream()
                .map(SameInterestProfileResponse::from).toList();
        return new SameAllInterestProfileResponse(sameInterestProfile);
    }
}
