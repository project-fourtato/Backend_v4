package com.hallym.booker.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchNicknameResultResponse {
    Long uid;
    String nickname;
    String userImageUrl;
    String usermessage;
}
