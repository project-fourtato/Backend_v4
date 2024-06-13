package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;
@Entity
@Getter
@Setter
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer followId;
    private Integer toUserId;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name= "from_user_id", referencedColumnName = "profile_uid")
    private Profile fromUserId;

    public void setProfile(Profile profile) {
        this.fromUserId = profile;
    }
    public Follow() {}
    public Follow(Integer toUserId) {
        this.toUserId = toUserId;
    }

    // 생성 메서드
    public static Follow create(Profile profile, Integer toUserId){
        Follow follows = new Follow(toUserId);
        follows.setProfile(profile);
        return follows;
    }
}