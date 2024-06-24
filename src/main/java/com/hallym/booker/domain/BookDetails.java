package com.hallym.booker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookDetails {

    @Id
    private String isbn;
    private String bookTitle;
    private String author;
    private String publisher;
    private String coverImageUrl;

    @OneToMany(mappedBy = "bookDetails") //유저가 저장한 책이 삭제되어도 책 상세정보는 그대로이다. 즉 종속적이지 않으므로 영속성 전이 코드 X
    private List<UserBooks> userBooks = new ArrayList<>();

    private BookDetails(String isbn, String bookTitle, String author, String publisher, String coverImageUrl) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.author = author;
        this.publisher = publisher;
        this.coverImageUrl = coverImageUrl;
    }

    // 생성 메서드
    public static BookDetails create(String isbn, String bookTitle, String author, String publisher, String coverImageUrl){
        BookDetails bookDetails = new BookDetails(isbn, bookTitle, author, publisher, coverImageUrl);
        return bookDetails;
    }

}
