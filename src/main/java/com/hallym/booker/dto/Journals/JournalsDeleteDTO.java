package com.hallym.booker.dto.Journals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JournalsDeleteDTO {
    private Long journalId;
}
