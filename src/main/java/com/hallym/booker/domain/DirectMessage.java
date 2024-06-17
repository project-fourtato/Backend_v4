package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class DirectMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private Integer mcheck;
    private String mtitle;
    private String mcontents;
    private LocalDateTime mdate;

    @OneToMany(mappedBy = "directMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileDirectM> profileDirectM = new ArrayList<>();

    private Integer senderUid;

    private Integer recipientUid;

    public void setProfileDirectM(ProfileDirectM profileDirectMs) {
        this.profileDirectM.add(profileDirectMs);
        profileDirectMs.setDirectMessage(this);
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
    public static DirectMessage create(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Integer senderUid, Integer recipientUid, ProfileDirectM profileDirectM){
        DirectMessage directMessage = new DirectMessage(mcheck, mtitle, mcontents, mdate);
        directMessage.setSenderUid(senderUid);
        directMessage.setRecipientUid(recipientUid);
        directMessage.setProfileDirectM(profileDirectM);

        return directMessage;
    }
}
