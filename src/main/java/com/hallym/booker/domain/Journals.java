package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journals {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long journalId;
    private String jtitle;
    private String jcontents;
    private LocalDateTime jdatetime;
    private String jimageUrl;
    private String jimageName;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "book_uid")
    private UserBooks userBooks;

    private Journals(String jtitle, String jcontents, LocalDateTime jdatetime,String jimageUrl, String jimageName) {
        this.jtitle = jtitle;
        this.jcontents = jcontents;
        this.jdatetime = jdatetime;
        this.jimageUrl = jimageUrl;
        this.jimageName = jimageName;
    }

    // 생성 메서드
    public static Journals create(UserBooks userBooks, String jtitle, String jcontents, LocalDateTime jdatetime,String jimageUrl, String jimageName){
        Journals journals = new Journals(jtitle, jcontents, jdatetime, jimageUrl, jimageName);
        journals.addUserBooks(userBooks);
        return journals;
    }

    //연관관계 편의 메서드
    private void addUserBooks(UserBooks userBooks) {
        this.userBooks = userBooks;
        userBooks.getJournals().add(this);
    }

    // 수정 메서드
    public Journals change(String jtitle, LocalDateTime jdatetime, String jcontents, String jimageUrl, String jimageName) {
        this.jtitle = jtitle;
        this.jdatetime = jdatetime;
        this.jcontents = jcontents;
        this.jimageUrl = jimageUrl;
        this.jimageName = jimageName;
        return this;
    }

}