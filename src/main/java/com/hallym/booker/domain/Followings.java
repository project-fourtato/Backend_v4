package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Followings {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followingsId;
    private Long followingUid;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name= "profile_uid")
    private Profile profile;

    private Followings(Long followingUid) {
        this.followingUid = followingUid;
    }

    // 생성 메서드
    public static Followings create(Profile profile, Long followingUid){
        Followings followings = new Followings(followingUid);
        followings.addProfile(profile);
        return followings;
    }

    //연관관계 편의 메서드
    private void addProfile(Profile profile){
        this.profile = profile;
        profile.getFollowings().add(this);
    }

}