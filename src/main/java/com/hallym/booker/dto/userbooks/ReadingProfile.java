package com.hallym.booker.dto.userbooks;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadingWithBookDetails {
    Long profileId;
    String userimageUrl;
    String nickname;
}
