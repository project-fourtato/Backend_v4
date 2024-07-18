package com.hallym.booker.dto.Journals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JournalsListResponseDTO {
    private Long journalId;
    private String jtitle;
    private String jcontents;
    private LocalDateTime jdatetime;
    private String jimageUrl;
    private String jimageName;
}
