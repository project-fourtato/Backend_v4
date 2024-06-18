package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ProfileDirectM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileDirectmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_uid")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private DirectMessage directMessage;

    // 생성자
    public ProfileDirectM() {}

    // 생성 메서드
    public static ProfileDirectM create(Profile profile, DirectMessage directMessage) {
        ProfileDirectM profileDirectM = new ProfileDirectM();
        profileDirectM.changeProfile(profile);
        profileDirectM.changeDirectMessage(directMessage);
        return profileDirectM;
    }

    //연관관계 편의 메서드
    private void changeProfile(Profile profile){
        this.profile = profile;
        profile.getProfileDirectMs().add(this);
    }
    private void changeDirectMessage(DirectMessage directMessage){
        this.directMessage = directMessage;
        directMessage.getProfileDirectM().add(this);
    }

}
