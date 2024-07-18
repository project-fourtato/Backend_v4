package com.hallym.booker.dto.Interests;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterestsResponseDTO {
    private Long interestUid;
    private String interestName;
}