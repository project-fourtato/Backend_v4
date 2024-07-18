package com.hallym.booker.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class RegisterRequest {
    MultipartFile file;
    String nickname;
    String usermessage;
    String uinterest1;
    String uinterest2;
    String uinterest3;
    String uinterest4;
    String uinterest5;
}
