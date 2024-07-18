package com.hallym.booker.dto.UserBooks;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestSellerResponse {
    String bookTitle;
    String bookAuthor;
    String isbn;
    String bookPublisher;
    String coverImg;
}
