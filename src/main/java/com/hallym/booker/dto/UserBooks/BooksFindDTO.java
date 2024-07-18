package com.hallym.booker.dto.UserBooks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksFindDTO {
    private String bookTitle;
    private String author;
    private String isbn;
    private String publisher;
    private String coverImageUrl;
}