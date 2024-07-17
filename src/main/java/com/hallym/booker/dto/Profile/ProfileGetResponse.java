package com.hallym.booker.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileGetResponse {
    Long uid;
    String nickname;
    String imageUrl;
    String imageName;
    String usermessage;
    List<String> interests;
}
