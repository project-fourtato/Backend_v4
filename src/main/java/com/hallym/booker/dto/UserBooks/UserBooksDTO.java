package com.hallym.booker.dto.UserBooks;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBooksDTO {
    private String profileUid;
    private String isbn;
    private Integer readStatus;
    private Integer saleStatus;
}
