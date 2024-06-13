package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Profile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profileUid;
    private String nickname;
    private String userImageUrl;
    private String userImageName;
    private String userMessage;

    // 외래키 참조
    @OneToOne
    @JoinColumn(name = "login_uid")
    private Login login;

    // 부모일 때 가지는 거
    @OneToMany(mappedBy = "profile")
    private List<UserBooks> userBooks = new ArrayList<>();

    @OneToMany(mappedBy = "profileUid")
    private List<Interests> interests = new ArrayList<>();

    @OneToMany(mappedBy = "fromUserId")
    private List<Follow> follow = new ArrayList<>();

    @OneToMany(mappedBy = "profileUid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileDirectM> profileDirectMs = new ArrayList<>();

    public void setLogin(Login login) {
        this.login = login;
    }

    public void setUserBooks(UserBooks userBooks) {
        this.userBooks.add(userBooks);
        userBooks.setProfile(this);
    }

    public void setInterests(Interests interests) {
        this.interests.add(interests);
        interests.setProfile(this);
    }
    public void setFollow(Follow follow) {
        this.follow.add(follow);
        follow.setProfile(this);
    }

    public void setProfileDirectM(ProfileDirectM profileDirectMs) {
        this.profileDirectMs.add(profileDirectMs);
        profileDirectMs.setProfile(this);
    }

    // 생성자
    public Profile() {}

    public Profile(String nickname, String userImageUrl, String userImageName, String userMessage) {
        this.nickname = nickname;
        this.userImageUrl = userImageUrl;
        this.userImageName = userImageName;
        this.userMessage = userMessage;
    }

    // 수정 메서드
    public Profile change(String userImageUrl, String userImageName, String userMessage) {
        this.userImageUrl = userImageUrl;
        this.userImageName = userImageName;
        this.userMessage = userMessage;

        return this;
    }

    // 생성 메서드
    public static Profile create(Login login, Follow follow, Interests interests, UserBooks userBooks, ProfileDirectM profileDirectMs, DirectMessage directMessages, String nickname, String userImageUrl, String userImageName, String userMessage) {
        Profile profile = new Profile(nickname, userImageUrl, userImageName, userMessage);
        profile.setLogin(login);

        if (userBooks != null) {
            profile.setUserBooks(userBooks);
        }
        if (profileDirectMs != null) {
            profile.setProfileDirectM(profileDirectMs);
        }
        if (follow != null) {
            profile.setFollow(follow);
        }
        profile.setLogin(login);
        if (interests != null) {
            profile.setInterests(interests);
        }

        return profile;
    }

}
