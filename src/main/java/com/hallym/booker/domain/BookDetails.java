package com.hallym.booker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookDetails {

    @Id
    private String isbn;
    private String bookTitle;
    private String author;
    private String publisher;
    private String coverImageUrl;

    // 부모일 때 가지는 거
    @OneToOne(mappedBy = "isbn")
    private UserBooks userBooks;

    public void setUserBooks(UserBooks userBooks) {
        this.userBooks = userBooks;
        userBooks.setBookDetails(this);
    }

    // 생성자
    public BookDetails() {}

    public BookDetails(String isbn, String bookTitle, String author, String publisher, String coverImageUrl) {
        this.isbn = isbn;
        this.bookTitle = bookTitle;
        this.author = author;
        this.publisher = publisher;
        this.coverImageUrl = coverImageUrl;
    }

    // 생성 메서드
    public static BookDetails create(UserBooks userBooks, String isbn, String bookTitle, String author, String publisher, String coverImageUrl){
        BookDetails bookDetails = new BookDetails(isbn, bookTitle, author, publisher, coverImageUrl);
        if(userBooks != null) {
            bookDetails.setUserBooks(userBooks);
        }

        return bookDetails;
    }

}
