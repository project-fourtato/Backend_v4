package com.hallym.booker.dto.journals;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class JournalSaveRequest {
    Long bookUid;
    String jtitle;
    String jcontents;
    String jimageUrl;
    String jimageName;
}
