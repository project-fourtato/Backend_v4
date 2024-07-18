package com.hallym.booker.dto.UserBooks;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBooksUpdateRequestDTO {
    private Integer readStatus;
    private Integer saleStatus;
}
