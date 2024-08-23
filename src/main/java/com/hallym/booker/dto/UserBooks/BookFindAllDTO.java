package com.hallym.booker.dto.UserBooks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFindAllDTO {
    List<BookFindDTO> result;

    public static BookFindAllDTO of(List<BookFindDTO> bookFindDTOList) {
        return new BookFindAllDTO(bookFindDTOList);
    }
}
