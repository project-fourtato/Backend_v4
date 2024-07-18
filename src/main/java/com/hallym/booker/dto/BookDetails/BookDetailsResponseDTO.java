package com.hallym.booker.dto.BookDetails;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailsResponseDTO {
    private String isbn;
    private String bookTitle;
    private String author;
    private String publisher;
    private String coverImageUrl;
}
