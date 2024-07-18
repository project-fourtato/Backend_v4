package com.hallym.booker.domain;

import com.hallym.booker.exception.profile.FollowCountException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileUid;
    private String nickname;
    private String userimageUrl;
    private String userimageName;
    private String usermessage;
    private Long countFollowers;
    private Long countFollowings;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "login_uid")
    private Login login;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBooks> userBooks = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interests> interests = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follow = new ArrayList<>();


    private Profile(String nickname, String userImageUrl, String userImageName, String userMessage) {
        this.nickname = nickname;
        this.userimageUrl = userImageUrl;
        this.userimageName = userImageName;
        this.usermessage = userMessage;
        this.countFollowers = 0L;
        this.countFollowings = 0L;
    }

    // 생성 메서드
    public static Profile create(Login login, String nickname, String userImageUrl, String userImageName, String userMessage) {
        Profile profile = new Profile(nickname, userImageUrl, userImageName, userMessage);
        profile.addLogin(login);
        return profile;
    }

    //연관관계 편의메서드
    private void addLogin(Login login) {
        this.login = login;
        login.setProfile(this);
    }

    // 수정 메서드
    public Profile change(String userImageUrl, String userImageName, String userMessage) {
        this.userimageUrl = userImageUrl;
        this.userimageName = userImageName;
        this.usermessage = userMessage;

        return this;
    }

    //비지니스 로직
    public void addFollowings(){
        this.countFollowings += 1;
    }

    public void addFollowers(){
        this.countFollowers += 1;
    }

    public void removeFollowings(){
        Long restFollowings = this.countFollowings-1;
        if (restFollowings < 0) {
            throw new FollowCountException("need more following");
        }
        this.countFollowings -= 1;
    }

    public void removeFollowers(){
        Long restFollowers = this.countFollowers-1;
        if (restFollowers < 0) {
            throw new FollowCountException("need more follower");
        }
        this.countFollowers -= 1;
    }

    public void removeFollow(Follow follow){
        this.getFollow().remove(follow);
    }
}
