package com.hallym.booker.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class UserBooks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookUid;
    private Integer readStatus;
    private Integer saleStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "isbn")
    private BookDetails bookDetails;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_uid")
    private Profile profile;

    @OneToMany(mappedBy = "userBooks", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Journals> journals = new ArrayList<>();

    // 생성자
    protected UserBooks() {}

    private UserBooks(Integer readStatus, Integer saleStatus) {
        this.readStatus = readStatus;
        this.saleStatus = saleStatus;
    }

    // 생성 메서드
    public static UserBooks create(Profile profile, BookDetails bookDetails, Integer readStatus, Integer saleStatus) {
        UserBooks userBooks = new UserBooks(readStatus, saleStatus);
        userBooks.addProfile(profile);
        userBooks.addBookDetails(bookDetails);
        return userBooks;
    }

    //연관관계 편의 메서드
    private void addProfile(Profile profile){
        this.profile = profile;
        profile.getUserBooks().add(this);
    }
    private void addBookDetails(BookDetails bookDetails){
        this.bookDetails = bookDetails;
        bookDetails.getUserBooks().add(this);
    }

    // 수정 메서드
    public UserBooks change(Integer readStatus, Integer saleStatus) {
        this.readStatus = readStatus;
        this.saleStatus = saleStatus;
        return this;
    }

}
