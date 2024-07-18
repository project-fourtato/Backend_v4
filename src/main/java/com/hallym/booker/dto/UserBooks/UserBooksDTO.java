package com.hallym.booker.dto.UserBooks;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBooksDTO {
    private Long profileUid;
    private String isbn;
    private Integer readStatus;
    private Integer saleStatus;
    private String bookTitle; // 추가: 책 제목
    private String author;    // 추가: 저자
    private String publisher; // 추가: 출판사
    private String coverImageUrl; // 추가: 표지 이미지 URL
}
