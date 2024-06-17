package com.hallym.booker.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class UserBooks {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookUid;
    private Integer readStatus;
    private Integer saleStatus;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "isbn")
    private BookDetails bookDetails;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_uid")
    private Profile profile;

    // 부모일 때 가지는 거
    @OneToMany(mappedBy = "userBooks")
    private List<Journals> journals = new ArrayList<>();

    public void setBookDetails(BookDetails bookDetails) {
        this.bookDetails = bookDetails;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setJournals(Journals journals) {
        this.journals.add(journals);
        journals.setUserBooks(this);
    }

    // 생성자
    public UserBooks() {}

    public UserBooks(Integer readStatus, Integer saleStatus) {
        this.readStatus = readStatus;
        this.saleStatus = saleStatus;
    }

    // 수정 메서드
    public UserBooks change(Integer readStatus, Integer saleStatus) {
        this.readStatus = readStatus;
        this.saleStatus = saleStatus;

        return this;
    }

    // 생성 메서드
    public static UserBooks create(Profile profile, BookDetails bookDetails, Journals journals, Integer readStatus, Integer saleStatus) {
        UserBooks userBooks = new UserBooks(readStatus, saleStatus);
        userBooks.setProfile(profile);
        userBooks.setBookDetails(bookDetails);
        if(journals != null) {
            userBooks.setJournals(journals);
        }

        return userBooks;
    }
}
