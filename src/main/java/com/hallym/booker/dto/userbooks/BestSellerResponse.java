package com.hallym.booker.dto.userbooks;

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
