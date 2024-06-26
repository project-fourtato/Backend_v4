package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Directmessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private Integer mcheck;
    private String mtitle;
    private String mcontents;
    private LocalDateTime mdate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_uid")
    private Profile profile;
    private Long recipientUid;

    private Directmessage(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Long recipientUid) {
        this.mcheck = mcheck;
        this.mtitle = mtitle;
        this.mcontents = mcontents;
        this.mdate = mdate;
        this.recipientUid = recipientUid;
    }

    // 생성 메서드
    public static Directmessage create(Profile profile, Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Long recipientUid){
        Directmessage directMessage = new Directmessage(mcheck, mtitle, mcontents, mdate, recipientUid);
        directMessage.addProfile(profile);
        return directMessage;
    }

    //연관관계 편의 메서드
    private void addProfile(Profile profile) {
        this.profile = profile;
        profile.getDirectmessages().add(this);
    }

    // 수정 메서드
    public Directmessage change(Integer mcheck) {
        this.mcheck = mcheck;
        return this;
    }
}
