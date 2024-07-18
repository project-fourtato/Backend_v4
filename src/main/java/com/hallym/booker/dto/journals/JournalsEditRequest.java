package com.hallym.booker.dto.journals;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class JournalsEditRequest {
    Long journalId;
    String jtitle;
    String jcontents;
    MultipartFile file;
}
