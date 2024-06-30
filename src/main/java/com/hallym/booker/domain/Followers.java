package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Followers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followersId;
    private Long followerUid;

    // 외래키 참조
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name= "profile_uid")
    private Profile profile;

    private Followers(Long followerUid) {
        this.followerUid = followerUid;
    }

    // 생성 메서드
    public static Followers create(Profile profile, Long followerUid){
        Followers followers = new Followers(followerUid);
        followers.addProfile(profile);
        return followers;
    }

    //연관관계 편의 메서드
    private void addProfile(Profile profile){
        this.profile = profile;
        profile.getFollowers().add(this);
    }
}
