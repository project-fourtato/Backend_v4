package com.hallym.booker.dto.Profile;

import com.hallym.booker.domain.Interests;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileEditResponse {
    String nickname;
    String imageUrl;
    String imageName;
    String usermessage;
    List<String> interests;
}
