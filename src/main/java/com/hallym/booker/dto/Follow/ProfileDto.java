package com.hallym.booker.dto.Follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileDto {
    private String loginId;
    private String nickname;
    private String userimageUrl;
    private String userimageName;
    private String usermessage;
}

