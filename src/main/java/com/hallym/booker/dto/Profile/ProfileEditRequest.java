package com.hallym.booker.dto.Profile;

import com.hallym.booker.domain.Interests;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileEditRequest {
    MultipartFile file;
    String usermessage;
    List<String> interests;
}
