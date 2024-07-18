package com.hallym.booker.dto.Journals;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JournalsEditFormResponse {
    Long journalId;
    LocalDateTime jdatetime;
    String jtitle;
    String jcontents;
    String jimageUrl;
    String jimageName;
}
