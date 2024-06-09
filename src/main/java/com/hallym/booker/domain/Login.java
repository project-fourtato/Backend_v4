package com.hallym.booker.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Login {

    @Id
    private String loginUid;
    private String pw;
    private String email;
    private String birth;

    // 부모일 때 가지는 거
    // foreign key constraint fails 에러를 위한 생쿼리문
    @OneToOne(mappedBy = "login", cascade = CascadeType.REMOVE)
    private Profile profile;

    public void setProfile(Profile profile) {
        this.profile = profile;
        profile.setLogin(this);
    }

    // 생성자
    public Login() {}

    public Login(String loginUid, String pw, String email, String birth) {
        this.loginUid = loginUid;
        this.pw = pw;
        this.email = email;
        this.birth = birth;
    }

    // 수정 메서드
    public Login change(String pw, String email, String birth) {
        this.pw = pw;
        this.email = email;
        this.birth = birth;

        return this;
    }

    // 생성 메서드
    public static Login create(Profile profile, String loginUid, String pw, String email, String birth){
        Login login = new Login(loginUid, pw, email, birth);
        login.setProfile(profile);

        return login;
    }
}
