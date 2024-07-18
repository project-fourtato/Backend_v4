package com.hallym.booker.dto.Journals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JournalsBookInfoResponseDTO {
    private String bookTitle;
    private String author;
    private String publisher;
    private String coverImageUrl;
    private String pubDate;
    private String description;
    private String categoryName;
}
