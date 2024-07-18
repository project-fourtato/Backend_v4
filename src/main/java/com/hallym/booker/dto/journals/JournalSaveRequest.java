package com.hallym.booker.dto.Journals;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JournalSaveRequest {
    Long bookUid;
    String jtitle;
    String jcontents;
    String jimageUrl;
    String jimageName;
}
