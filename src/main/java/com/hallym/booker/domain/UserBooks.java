package com.hallym.booker.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToMany(mappedBy = "userBooks")
    private List<Journals> journals = new ArrayList<>();

    // 생성자
    public UserBooks() {}

    public UserBooks(Integer readStatus, Integer saleStatus) {
        this.readStatus = readStatus;
        this.saleStatus = saleStatus;
    }

    // 생성 메서드
    public static UserBooks create(Profile profile, BookDetails bookDetails, Integer readStatus, Integer saleStatus) {
        UserBooks userBooks = new UserBooks(readStatus, saleStatus);
        userBooks.changeProfile(profile);
        userBooks.changeBookDetails(bookDetails);
        return userBooks;
    }

    //연관관계 편의 메서드
    private void changeProfile(Profile profile){
        this.profile = profile;
        profile.getUserBooks().add(this);
    }
    private void changeBookDetails(BookDetails bookDetails){
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
