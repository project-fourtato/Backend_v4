package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProfileDirectM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profileDirectMId;

    // 이건... GPT의 도움을 받은 거라... 이렇게 해도 될지 모르겠네 ㅠㅠㅠ
    @ManyToOne
    @JoinColumn(name = "profile_uid")
    private Profile profile;

    @OneToOne
    @JoinColumn(name = "message_id")
    private DirectMessage directMessage;

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setDirectMessage(DirectMessage directMessage) {
        this.directMessage = directMessage;
    }

    // 생성자
    public ProfileDirectM() {}

    // 생성 메서드
    public static ProfileDirectM create(Profile profile, DirectMessage directMessage) {
        ProfileDirectM profileDirectM = new ProfileDirectM();
        profileDirectM.setProfile(profile);
        profileDirectM.setDirectMessage(directMessage);

        return profileDirectM;
    }
}
