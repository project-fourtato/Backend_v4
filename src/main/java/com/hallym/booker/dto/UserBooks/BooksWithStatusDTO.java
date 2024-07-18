package com.hallym.booker.dto.UserBooks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksWithStatusDTO {
    private String isbn;
    private String bookTitle;
    private String author;
    private String publisher;
    private String coverImageUrl;
    private Integer readStatus;
}
