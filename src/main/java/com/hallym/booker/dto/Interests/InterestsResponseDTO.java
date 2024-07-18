package com.hallym.booker.dto.Interests;

import lombok.*;

@Data
@Builder
public class InterestsResponseDTO {
    private Long interestUid;
    private String interestName;
}