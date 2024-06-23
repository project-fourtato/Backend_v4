package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
@Entity
@Getter
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;
    private Long toUserId;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name= "from_user_id")
    private Profile profile;

    protected Follow() {}
    private Follow(Long toUserId) {
        this.toUserId = toUserId;
    }

    // 생성 메서드
    public static Follow create(Profile profile, Long toUserId){
        Follow follows = new Follow(toUserId);
        follows.addProfile(profile);
        return follows;
    }

    //연관관계 편의 메서드
    private void addProfile(Profile profile){
        this.profile = profile;
        profile.getFollow().add(this);
    }

}