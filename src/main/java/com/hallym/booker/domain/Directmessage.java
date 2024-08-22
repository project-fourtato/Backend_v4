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
    private Long senderUid;
    private Long recipientUid;
    private boolean deleteSenderCheck;
    private boolean deleteRecipientCheck;

    private Directmessage(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Long senderUid, Long recipientUid) {
        this.mcheck = mcheck;
        this.mtitle = mtitle;
        this.mcontents = mcontents;
        this.mdate = mdate;
        this.senderUid = senderUid;
        this.recipientUid = recipientUid;
        this.deleteSenderCheck = false;
        this.deleteRecipientCheck = false;
    }

    // 생성 메서드
    public static Directmessage create(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Long senderUid, Long recipientUid){
        Directmessage directMessage = new Directmessage(mcheck, mtitle, mcontents, mdate, senderUid, recipientUid);
        return directMessage;
    }

    // 비지니스 로직
    public Directmessage changeMCheck(Integer mcheck) {
        this.mcheck = mcheck;
        return this;
    }

    public Directmessage changeDeleteSenderCheck() {
        this.deleteSenderCheck = true;
        return this;
    }

    public Directmessage changeDeleteRecipientCheck() {
        this.deleteRecipientCheck = true;
        return this;
    }

    public Directmessage changeRecipientUid() {
        this.recipientUid = -1L;
        return this;
    }

    public Directmessage changeSenderUid() {
        this.senderUid = -1L;
        return this;
    }
}
