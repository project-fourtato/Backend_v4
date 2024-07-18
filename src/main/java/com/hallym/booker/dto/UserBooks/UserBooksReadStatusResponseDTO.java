package com.hallym.booker.dto.UserBooks;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBooksReadStatusResponseDTO {
    private Long bookUid;
    private Integer readStatus;
    private Integer saleStatus;
}
