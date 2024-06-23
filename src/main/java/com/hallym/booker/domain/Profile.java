package com.hallym.booker.domain;

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "login_uid")
    private Login login;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBooks> userBooks = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interests> interests = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follow = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileDirectM> profileDirectMs = new ArrayList<>();

    private Profile(String nickname, String userImageUrl, String userImageName, String userMessage) {
        this.nickname = nickname;
        this.userimageUrl = userImageUrl;
        this.userimageName = userImageName;
        this.usermessage = userMessage;
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

}
