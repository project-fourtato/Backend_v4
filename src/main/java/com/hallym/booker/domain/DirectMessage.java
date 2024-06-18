package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class DirectMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private Integer mcheck;
    private String mtitle;
    private String mcontents;
    private LocalDateTime mdate;

    @OneToMany(mappedBy = "directMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileDirectM> profileDirectM = new ArrayList<>();

    private Long senderUid;

    private Long recipientUid;

    public DirectMessage() {}

    public DirectMessage(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Long senderUid, Long recipientUid) {
        this.mcheck = mcheck;
        this.mtitle = mtitle;
        this.mcontents = mcontents;
        this.mdate = mdate;
        this.senderUid = senderUid;
        this.recipientUid = recipientUid;
    }

    // 생성 메서드
    public static DirectMessage create(Integer mcheck, String mtitle, String mcontents, LocalDateTime mdate, Long senderUid, Long recipientUid){
        DirectMessage directMessage = new DirectMessage(mcheck, mtitle, mcontents, mdate, senderUid, recipientUid);
        return directMessage;
    }

    // 수정 메서드
    public DirectMessage change(Integer mcheck) {
        this.mcheck = mcheck;
        return this;
    }
}
