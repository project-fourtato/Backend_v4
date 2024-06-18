package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Interests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestUid;

    private String interestName;

    // 외래키 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_uid")
    private Profile profile;

    // 생성자
    public Interests() {}

    public Interests(String interestName) {
        this.interestName = interestName;
    }

    // 수정 메서드
    public Interests update(String interestName) {
        this.interestName = interestName;
        return this;
    }

    // 생성 메서드
    public static Interests create(String interestName, Profile profile){
        Interests interests = new Interests(interestName);
        interests.changeProfile(profile);
        return interests;
    }

    //연관관계 편의 메서드
    private void changeProfile(Profile profile){
        this.profile = profile;
        profile.getInterests().add(this);
    }

}
