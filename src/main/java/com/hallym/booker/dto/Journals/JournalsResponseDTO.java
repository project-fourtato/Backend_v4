package com.hallym.booker.dto.Journals;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JournalsResponseDTO {
    private Long journalId;
    private String jtitle;
    private String jcontents;
    private LocalDateTime jdatetime;
    private String jimageUrl;
    private String jimageName;
}
