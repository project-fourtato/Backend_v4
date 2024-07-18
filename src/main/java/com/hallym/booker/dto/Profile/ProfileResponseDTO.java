package com.hallym.booker.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDTO {
    private Long profileUid;
    private String nickname;
    private String userimageUrl;
    private String userimageName;
    private String usermessage;
}
