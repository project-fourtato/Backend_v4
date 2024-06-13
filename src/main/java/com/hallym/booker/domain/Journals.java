package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
@Entity
@Getter
@Setter
public class Journals {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer journalId;
    private String jtitle;
    private String jcontents;
    private LocalDateTime jdatetime;
    private String jimageUrl;
    private String jimageName;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "book_uid")
    private UserBooks userBooks;

    public void setUserBooks(UserBooks userBooks) {
        this.userBooks = userBooks;
    }

    public Journals() {}
    public Journals(String jtitle, String jcontents, LocalDateTime jdatetime,String jimageUrl, String jimageName) {
        this.jtitle = jtitle;
        this.jcontents = jcontents;
        this.jdatetime = jdatetime;
        this.jimageUrl = jimageUrl;
        this.jimageName = jimageName;
    }

    // 수정 메서드
    public Journals change(String jtitle, String jcontents, String jimageUrl, String jimageName) {
        this.jtitle = jtitle;
        this.jcontents = jcontents;
        this.jimageUrl = jimageUrl;
        this.jimageName = jimageName;

        return this;
    }

    // 생성 메서드
    public static Journals create(UserBooks userBooks, String jtitle, String jcontents, LocalDateTime jdatetime,String jimageUrl, String jimageName){
        Journals journals = new Journals(jtitle, jcontents, jdatetime, jimageUrl, jimageName);
        journals.setUserBooks(userBooks);

        return journals;
    }
}