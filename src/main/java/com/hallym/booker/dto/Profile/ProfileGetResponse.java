package com.hallym.booker.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileGetResponse {
    String nickname;
    String imageUrl;
    String imageName;
    String usermessage;
    List<String> interests;
}
