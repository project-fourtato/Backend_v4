package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DirectMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;
    private Integer mcheck;
    private String mtitle;
    private String mcontents;
    private LocalDateTime mdate;

    // 이건... GPT의 도움을 받은 거라... 이렇게 해도 될지 모르겠네 ㅠㅠㅠ
    @ManyToOne
    @JoinColumn(name = "sender_uid")
    private Profile senderUid;

    @ManyToOne
    @JoinColumn(name = "recipient_uid")
    private Profile recipientUid;

    // 부모일 때 가지는 거
    @OneToOne(mappedBy = "messageId")
    private ProfileDirectM profileDirectM;

    public void setSenderUid(Profile senderUid) {
        this.senderUid = senderUid;
    }

    public void setRecipientUid(Profile recipientUid) {
        this.recipientUid = recipientUid;
    }
    public void setProfileDirectM(ProfileDirectM profileDirectM) {
        this.profileDirectM = profileDirectM;
    }

    public DirectMessage() {}

    public DirectMessage(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate) {
        this.mcheck = mcheck;
        this.mtitle = mtitle;
        this.mcontents = mcontents;
        this.mdate = mdate;
    }

    // 수정 메서드
    public DirectMessage change(Integer mcheck) {
        this.mcheck = mcheck;

        return this;
    }

    // 생성 메서드
    public static DirectMessage create(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Profile senderUid, Profile recipientUid, ProfileDirectM profileDirectM){
        DirectMessage directMessage = new DirectMessage(mcheck, mtitle, mcontents, mdate);
        directMessage.setSenderUid(senderUid);
        directMessage.setRecipientUid(recipientUid);
        directMessage.setProfileDirectM(profileDirectM);

        return directMessage;
    }
}
